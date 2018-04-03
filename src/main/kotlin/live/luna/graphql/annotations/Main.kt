package live.luna.graphql.annotations

import graphql.schema.*
import java.lang.reflect.*

fun buildSchema(queryKlass: Klass, mutationKlass: Klass?): GraphQLSchema {
    val processorContext = ProcessorContext()

    val queryObject = processOutput(queryKlass, processorContext)

    val mutationObject = mutationKlass?.let {
        processOutput(it, processorContext)
    }

    return GraphQLSchema.newSchema().query(queryObject).mutation(mutationObject).build()
}

private fun processOutput(klass: Klass, context: ProcessorContext): GraphQLObjectType {
    val klassAnnotation = klass.getAnnotation(GraphQLObject::class.java)
            ?: throw GraphQLSchemaBuilderException("${klass.name} class isn't annotated with 'GraphQLObject', but expected to be")

    val name = klassAnnotation.name.ifEmptyThen(klass.simpleName)
    val description = klassAnnotation.description.ifEmptyThenNull()

    context.processOutput(klass, name)

    val builder = GraphQLObjectType.newObject()
            .name(name)
            .description(description)

    klass.declaredFields.forEach {
        processEntity(true, it, context)?.let { builder.field(it) }
    }
    klass.declaredMethods.forEach {
        processEntity(false, it, context)?.let { builder.field(it) }
    }

    val type = builder.build()
    context.knowOutput(klass, type)
    return type
}

private fun modifyType(type: GraphQLType, modifiers: Array<GraphQLModifier>): GraphQLType {
    return modifiers.foldRight(type) { wrap, oldType ->
        when (wrap) {
            GraphQLModifier.LIST -> GraphQLList(oldType)
            GraphQLModifier.NOT_NULL -> GraphQLNonNull(oldType)
        }
    }
}

private fun processEntity(isField: Boolean, member: AccessibleObject, context: ProcessorContext): GraphQLFieldDefinition? {
    val simpleAnnotation: GraphQLField? = member.getAnnotation(GraphQLField::class.java)
    val complexAnnotation: GraphQLComplexField? = member.getAnnotation(GraphQLComplexField::class.java)

    if (simpleAnnotation == null && complexAnnotation == null) {
        return null
    }

    if (simpleAnnotation != null && complexAnnotation != null) {
        throw GraphQLSchemaBuilderException("Annotations @GraphQLField and @GraphQLComplexField can't be combined")
    }

    val isSimple = simpleAnnotation != null

    member.isAccessible = true

    val klass = if (isField) (member as Field).type else (member as Method).returnType // this too
    val baseKlass = if (isSimple) klass else complexAnnotation!!.type.java // autodetect not null type, constraints

    if (context.getOutputType(baseKlass) == null) {
        processOutput(baseKlass, context)
    }

    val name = (if (isSimple) simpleAnnotation!!.name else complexAnnotation!!.name)
            .ifEmptyThen((member as Member).name)
    val description = (if (isSimple) simpleAnnotation!!.description else complexAnnotation!!.description)
            .ifEmptyThenNull()
    val baseType = context.getOutputType(baseKlass)
            ?: throw GraphQLSchemaBuilderException("Unexpected behaviour: cannot get type for ${baseKlass.name}")
    val type = modifyType(baseType,
            if (isSimple) (if (simpleAnnotation!!.nullable) arrayOf() else arrayOf(GraphQLModifier.NOT_NULL))
            else complexAnnotation!!.modifiers)

    val graphQLField = GraphQLFieldDefinition.Builder()
            .name(name)
            .description(description)
            .type(type as GraphQLOutputType)

    if (isField) {
        graphQLField.dataFetcher { env -> (member as Field).get(EnvironmentWrapper(env, member.declaringClass).source) } // runtime call
    } else {
        val (arguments, argumentInjectors) = processParameters(member as Method, context)
        graphQLField.argument(arguments)
        graphQLField.dataFetcher { env ->
            val wrapper = EnvironmentWrapper(env, member.declaringClass)
            member.invoke(wrapper.source, *argumentInjectors.map { it.invoke(wrapper) }.toTypedArray()) // runtime call
        }
    }

    return graphQLField.build()
}

@Suppress("UNCHECKED_CAST")
private fun processParameters(method: Method, context: ProcessorContext): MethodSignatureHolder {
    val arguments = mutableListOf<graphql.schema.GraphQLArgument>()
    val argumentInjectors = mutableListOf<(EnvironmentWrapper) -> Any?>()

    val sourceKlass = method.declaringClass
    method.parameters.forEach {
        val simpleAnnotation: GraphQLArgument? = it.getAnnotation(GraphQLArgument::class.java)
        val complexAnnotation: GraphQLComplexArgument? = it.getAnnotation(GraphQLComplexArgument::class.java)
        val contextAnnotation = it.getAnnotation(GraphQLContext::class.java)
        when {
            simpleAnnotation != null || complexAnnotation != null -> {
                if (simpleAnnotation != null && complexAnnotation != null) {
                    throw GraphQLSchemaBuilderException("Annotations @GraphQLArgument and @GraphQLComplexArgument can't be combined")
                }
                val isSimple = simpleAnnotation != null
                val name = if (isSimple) simpleAnnotation!!.name else complexAnnotation!!.name
                val description = (if (isSimple) simpleAnnotation!!.description else complexAnnotation!!.description).ifEmptyThenNull()
                val baseKlass = if (isSimple) it.type else complexAnnotation!!.type.java
                val (baseType, baseCreator) = getInputType(baseKlass, context)
                val type = modifyType(baseType,
                        if (isSimple) (if (simpleAnnotation!!.nullable) arrayOf() else arrayOf(GraphQLModifier.NOT_NULL))
                        else complexAnnotation!!.modifiers)
                val depth = if (isSimple) 0 else complexAnnotation!!.modifiers.filter { it == GraphQLModifier.LIST }.count()
                val creator = if (isSimple) baseCreator else { _, smth ->
                    instantiateArgument(smth as List<Any?>?, depth, 1, baseCreator)
                }

                val argument = graphql.schema.GraphQLArgument.Builder()
                        .name(name)
                        .description(description)
                        .type(type as GraphQLInputType)
                arguments.add(argument.build())
                argumentInjectors.add { env -> env(name).let { creator?.invoke(name, it) ?: it } } // runtime call
            }
            contextAnnotation != null -> argumentInjectors.add { env -> env.context }
            it.type == sourceKlass -> argumentInjectors.add { env -> env.source }
            it.type == DataFetchingEnvironment::class.java -> argumentInjectors.add { env -> env.environment }
            else -> throw GraphQLSchemaBuilderException("Parameter ${it.type} in method ${method.name} cannot be injected")
        }
    }
    return MethodSignatureHolder(arguments, argumentInjectors)
}

fun instantiateArgument(list: List<Any?>?, depth: Int, current: Int, creator: InputObjectCreator?): List<Any?>? {
    if (depth == current) return list?.map { creator?.invoke("", it) ?: it }
    return list?.map { instantiateArgument(it as List<Any?>, depth, current + 1, creator) }
}

private fun getInputType(klass: Klass, context: ProcessorContext): InputTypeWrapper {
    context.getInputType(klass)?.let { return it }
    if (context.isProcessingInput(klass)) {
        throw GraphQLSchemaBuilderException("Cyclic reference through ${klass.name} detected")
    }

    val c = klass.constructors.firstOrNull { it.isAnnotationPresent(GraphQLInputObject::class.java) }
            ?: throw GraphQLSchemaBuilderException("${klass.name} class doesn't have a constructor annotated with 'GraphQLInputObject', but expected to be")

    val annotation = c.getAnnotation(GraphQLInputObject::class.java)

    val name = annotation.name.ifEmptyThen(klass.simpleName)
    val description = annotation.description.ifEmptyThenNull()

    context.processInput(klass)

    val builder = GraphQLInputObjectType.newInputObject()
            .name(name)
            .description(description)

    val params = mutableListOf<Pair<String, InputObjectCreator?>>()
    c.parameters.forEach {
        val (field, creator) = processInputField(it, context)
                ?: throw GraphQLSchemaBuilderException("Cannot provide argument with type ${it.type} to ${klass.name} constructor")
        builder.field(field)
        params.add(creator)
    }

    val creator: InputObjectCreator = { _, map ->
        map?.let {
            c.newInstance(*params.map {
                (map as Map<String, Any>)[it.first]
                        ?.let { sb -> it.second?.invoke(it.first, sb) ?: sb } // runtime call
            }.toTypedArray())
        }
    }

    val type = builder.build()
    context.knowInput(klass, type, creator)
    return context.getInputType(klass)!!
}


fun processInputField(parameter: Parameter, context: ProcessorContext): InputFieldWrapper? {
    val annotation = parameter.getAnnotation(GraphQLInputField::class.java) ?: return null

    val klass = parameter.type
    val isSimpleList = (klass == List::class.java || klass.interfaces.contains(List::class.java)) && annotation.of != Void::class
    val baseKlass = if (isSimpleList) annotation.of.java else klass

    val name = annotation.name
    val description = annotation.description.ifEmptyThenNull()
    val (baseType, creator) = getInputType(baseKlass, context)
    val baseTypeNullable = annotation.ofNullable
    val type = if (isSimpleList) GraphQLList(if (baseTypeNullable) baseType else GraphQLNonNull(baseType)) else baseType
    val nullable = annotation.nullable

    val graphQLField = GraphQLInputObjectField.Builder()
            .name(name)
            .description(description)
            .type(if (nullable) type else GraphQLNonNull(type))

    return InputFieldWrapper(graphQLField.build(), Pair(name, creator))
}
