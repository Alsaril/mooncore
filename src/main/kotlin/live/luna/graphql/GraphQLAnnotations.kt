package live.luna.graphql

import graphql.Scalars
import graphql.Scalars.*
import graphql.schema.*
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KClass

// могут быть проблемы с одинаково названными типами
// для методов дописать список в качестве возвращаемого типа
// вынести создание нового типа в отдельную функцию (в 4 местах!)

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class GraphQLObject(
        val name: String = "",
        val description: String = ""
)

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class GraphQLField(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false,
        val of: KClass<*> = Void::class,
        val ofNullable: Boolean = false
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class Argument(
        val name: String,
        val description: String = "",
        val nullable: Boolean = false
)

enum class GraphQLModifier {
    NOT_NULL, LIST
}

class GraphQLSchemaBuilderException(message: String, throwable: Throwable? = null) : Exception(message, throwable)

fun buildSchema(queryKlass: Klass?, mutationKlass: Klass?): GraphQLSchema {
    if (queryKlass == null && mutationKlass == null) {
        throw GraphQLSchemaBuilderException("Both query and mutation are null")
    }

    val processorContext = ProcessorContext()

    val queryObject = queryKlass?.let {
        process(it, processorContext)
    }

    val mutationObject = mutationKlass?.let {
        process(it, processorContext)
    }

    return GraphQLSchema.newSchema().query(queryObject).mutation(mutationObject).build()
}

typealias Klass = Class<out Any>

class ProcessorContext(private val knownTypes: MutableMap<Klass, GraphQLOutputType> = mutableMapOf(),
                       private val processingTypes: MutableMap<Klass, String> = mutableMapOf()) {
    init {
        knownTypes[Int::class.java] = Scalars.GraphQLInt
        knownTypes[Long::class.java] = Scalars.GraphQLLong
        knownTypes[String::class.java] = Scalars.GraphQLString
        knownTypes[Boolean::class.java] = Scalars.GraphQLBoolean
        knownTypes[Double::class.java] = Scalars.GraphQLFloat
        knownTypes[Date::class.java] = Scalars.GraphQLString
    }

    fun getType(klass: Klass): GraphQLOutputType? = knownTypes[klass]
            ?: processingTypes[klass]?.let { GraphQLTypeReference(it) }

    fun process(klass: Klass, name: String) {
        processingTypes[klass] = name
    }

    fun know(klass: Klass, type: GraphQLOutputType) {
        processingTypes.remove(klass)
        knownTypes[klass] = type
    }
}

private fun <T> process(klass: Class<out T>, context: ProcessorContext): GraphQLObjectType {
    val klassAnnotation = klass.getAnnotation(GraphQLObject::class.java)
            ?: throw GraphQLSchemaBuilderException("${klass.name} class isn't annotated with 'GraphQLObject', but expected to be")

    val name = klassAnnotation.name.ifEmptyThen(klass.simpleName)
    val description = klassAnnotation.description.ifEmptyThenNull()

    context.process(klass, name)

    val builder = GraphQLObjectType.newObject()
            .name(name)
            .description(description)

    builder.fields(processFields(klass.declaredFields, context))
    builder.fields(processMethods(klass.declaredMethods, context))

    val type = builder.build()
    context.know(klass, type)
    return type
}

private fun processFields(fields: Array<Field>, context: ProcessorContext): List<GraphQLFieldDefinition> {
    val result = mutableListOf<GraphQLFieldDefinition>()
    fields.forEach {
        val annotation = it.getAnnotation(GraphQLField::class.java) ?: return@forEach

        it.isAccessible = true

        val isSimpleList = (it.type == List::class.java || it.type.interfaces.contains(List::class.java)) && annotation.of != Void::class
        val baseKlass = if (isSimpleList) annotation.of.java else it.type

        if (context.getType(baseKlass) == null) {
            process(baseKlass, context)
        }

        val name = annotation.name.ifEmptyThen(it.name)
        val description = annotation.description.ifEmptyThenNull()
        val baseType = context.getType(baseKlass)
                ?: throw GraphQLSchemaBuilderException("Unexpected behaviour: cannot get type for ${it.type.name}")
        val baseTypeNullable = annotation.ofNullable
        val type = if (isSimpleList) GraphQLList(if (baseTypeNullable) baseType else GraphQLNonNull(baseType)) else baseType
        val nullable = annotation.nullable

        val graphQLField = GraphQLFieldDefinition.Builder()
                .name(name)
                .description(description)
                .type(if (nullable) type else GraphQLNonNull(type))
                .dataFetcher { env -> it.get(EnvironmentWrapper(env, it.declaringClass).source) }

        result.add(graphQLField.build())
    }
    return result
}

private fun processMethods(methods: Array<Method>, context: ProcessorContext): List<GraphQLFieldDefinition> {
    val result = mutableListOf<GraphQLFieldDefinition>()
    methods.forEach { method ->
        val annotation = method.getAnnotation(GraphQLField::class.java) ?: return@forEach

        if (context.getType(method.returnType) == null) {
            process(method.returnType, context)
        }

        method.isAccessible = true

        val (arguments, argumentInjectors) = processParameters(method)

        val name = annotation.name.ifEmptyThen(method.name)
        val description = annotation.description.ifEmptyThenNull()
        val type = context.getType(method.returnType)
                ?: throw GraphQLSchemaBuilderException("Unexpected behaviour: cannot get type for ${method.returnType.name}")
        val nullable = annotation.nullable

        val graphQLField = GraphQLFieldDefinition.Builder().name(name)
                .description(description)
                .argument(arguments)
                .type(if (nullable) type else GraphQLNonNull(type))
                .dataFetcher { env ->
                    val wrapper = EnvironmentWrapper(env, method.declaringClass)
                    method.invoke(wrapper.source, *argumentInjectors.map { it.invoke(wrapper) }.toTypedArray())
                }
        result.add(graphQLField.build())
    }
    return result
}

private data class MethodSignatureHolder(val arguments: List<GraphQLArgument>, val argumentInjectors: List<(EnvironmentWrapper) -> Any>)

private fun processParameters(method: Method): MethodSignatureHolder {
    val arguments = mutableListOf<GraphQLArgument>()
    val argumentInjectors = mutableListOf<(EnvironmentWrapper) -> Any>()

    val sourceKlass = method.declaringClass
    val parameterAnnotation = method.getAnnotation(Argument::class.java)
    method.parameters.forEach {
        when {
            parameterAnnotation != null -> {
                val name = parameterAnnotation.name
                val description = parameterAnnotation.description.ifEmptyThenNull()
                val nullable = parameterAnnotation.nullable
                val type = getInputType(it.type)

                val argument = GraphQLArgument.Builder()
                        .name(name)
                        .description(description)
                        .type(if (nullable) type else GraphQLNonNull(type))
                arguments.add(argument.build())
                argumentInjectors.add { env -> env(name) }
            }
            it.type == sourceKlass -> argumentInjectors.add { env -> env.source }
            else -> throw GraphQLSchemaBuilderException("Parameter ${it.type} in method ${method.name} cannot be injected")
        }
    }
    return MethodSignatureHolder(arguments.toList(), argumentInjectors.toList())
}

private class EnvironmentWrapper(private val environment: DataFetchingEnvironment, klass: Klass) {
    val source: Any = environment.getSource() ?: klass.newInstance()
    operator fun invoke(name: String): Any = environment.getArgument<Any>(name)
}

private fun getInputType(klass: Klass): GraphQLInputType = when (klass) {
    Int::class.java -> GraphQLInt
    Long::class.java -> GraphQLLong
    String::class.java -> GraphQLString
    Boolean::class.java -> GraphQLBoolean
    else -> throw GraphQLSchemaBuilderException("Unknown input type ${klass.name}")
}

inline fun String.ifEmptyThen(default: String) = if (!isEmpty()) this else default
inline fun String.ifEmptyThenNull() = if (!isEmpty()) this else null