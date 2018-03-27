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

private fun processEntity(isField: Boolean, member: AccessibleObject, context: ProcessorContext): GraphQLFieldDefinition? {
    val annotation = member.getAnnotation(GraphQLField::class.java) ?: return null

    member.isAccessible = true

    val klass = if (isField) (member as Field).type else (member as Method).returnType
    val isSimpleList = (klass == List::class.java || klass.interfaces.contains(List::class.java)) && annotation.of != Void::class
    val baseKlass = if (isSimpleList) annotation.of.java else klass

    if (context.getOutputType(baseKlass) == null) {
        processOutput(baseKlass, context)
    }

    val name = annotation.name.ifEmptyThen((member as Member).name)
    val description = annotation.description.ifEmptyThenNull()
    val baseType = context.getOutputType(baseKlass)
            ?: throw GraphQLSchemaBuilderException("Unexpected behaviour: cannot get type for ${baseKlass.name}")
    val baseTypeNullable = annotation.ofNullable
    val type = if (isSimpleList) GraphQLList(if (baseTypeNullable) baseType else GraphQLNonNull(baseType)) else baseType
    val nullable = annotation.nullable

    val graphQLField = GraphQLFieldDefinition.Builder()
            .name(name)
            .description(description)
            .type(if (nullable) type else GraphQLNonNull(type))

    if (isField) {
        graphQLField.dataFetcher { env -> (member as Field).get(EnvironmentWrapper(env, member.declaringClass).source) }
    } else {
        val (arguments, argumentInjectors) = processParameters(member as Method, context)
        graphQLField.argument(arguments)
        graphQLField.dataFetcher { env ->
            val wrapper = EnvironmentWrapper(env, member.declaringClass)
            member.invoke(wrapper.source, *argumentInjectors.map { it.invoke(wrapper) }.toTypedArray())
        }
    }

    return graphQLField.build()
}

@Suppress("UNCHECKED_CAST")
private fun processParameters(method: Method, context: ProcessorContext): MethodSignatureHolder {
    val arguments = mutableListOf<graphql.schema.GraphQLArgument>()
    val argumentInjectors = mutableListOf<(EnvironmentWrapper) -> Any>()

    val sourceKlass = method.declaringClass
    method.parameters.forEach {
        val parameterAnnotation = it.getAnnotation(GraphQLArgument::class.java)
        val contextAnnotation = it.getAnnotation(GraphQLContext::class.java)
        when {
            parameterAnnotation != null -> {
                val name = parameterAnnotation.name
                val description = parameterAnnotation.description.ifEmptyThenNull()
                val nullable = parameterAnnotation.nullable
                val (type, creator) = getInputType(it.type, context)

                val argument = graphql.schema.GraphQLArgument.Builder()
                        .name(name)
                        .description(description)
                        .type(if (nullable) type else GraphQLNonNull(type))
                arguments.add(argument.build())
                argumentInjectors.add { env -> env(name).let { creator?.invoke(name, it) ?: it } }
            }
            contextAnnotation != null -> argumentInjectors.add { env -> env.context }
            it.type == sourceKlass -> argumentInjectors.add { env -> env.source }
            it.type == DataFetchingEnvironment::class.java -> argumentInjectors.add { env -> env.environment }
            else -> throw GraphQLSchemaBuilderException("Parameter ${it.type} in method ${method.name} cannot be injected")
        }
    }
    return MethodSignatureHolder(arguments.toList(), argumentInjectors.toList())
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

    val creator: InputObjectCreator = { name, map ->
        c.newInstance(*params.map {
            val subObject = (map as Map<String, Any>)[it.first]!!
            it.second?.invoke(it.first, subObject) ?: subObject
        }.toTypedArray())
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