package live.luna.graphql

import graphql.Scalars
import graphql.Scalars.*
import graphql.schema.*
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KClass

// могут быть проблемы с одинаково названными типами

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

    val fields = mutableListOf<GraphQLFieldDefinition>()
    klass.declaredFields.forEach {
        processEntity(true, it, context)?.let { fields.add(it) }
    }
    klass.declaredMethods.forEach {
        processEntity(false, it, context)?.let { fields.add(it) }
    }

    val type = builder.fields(fields).build()
    context.know(klass, type)
    return type
}

private fun processEntity(isField: Boolean, member: AccessibleObject, context: ProcessorContext): GraphQLFieldDefinition? {
    val annotation = member.getAnnotation(GraphQLField::class.java) ?: return null

    member.isAccessible = true

    val klass = if (isField) (member as Field).type else (member as Method).returnType
    val isSimpleList = (klass == List::class.java || klass.interfaces.contains(List::class.java)) && annotation.of != Void::class
    val baseKlass = if (isSimpleList) annotation.of.java else klass

    if (context.getType(baseKlass) == null) {
        process(baseKlass, context)
    }

    val name = annotation.name.ifEmptyThen((member as Member).name)
    val description = annotation.description.ifEmptyThenNull()
    val baseType = context.getType(baseKlass)
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
        val (arguments, argumentInjectors) = processParameters(member as Method)
        graphQLField.argument(arguments)
        graphQLField.dataFetcher { env ->
            val wrapper = EnvironmentWrapper(env, member.declaringClass)
            member.invoke(wrapper.source, *argumentInjectors.map { it.invoke(wrapper) }.toTypedArray())
        }
    }

    return graphQLField.build()
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