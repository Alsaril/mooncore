package live.luna.graphql_object

import graphql.schema.*

@DslMarker
@Target(AnnotationTarget.TYPE)
annotation class DSL

class FieldBuilder<T> {
    val fields = mutableListOf<GraphQLFieldDefinition>()

    fun nullableA(name: String, type: GraphQLOutputType, description: String? = null, argumentCreator: (@DSL ArgumentBuilder).() -> Unit): (((@DSL T?).(ArgumentHolder) -> Any?)) -> Unit {
        return { dataFetcher: T?.(ArgumentHolder) -> Any? ->
            val field = GraphQLFieldDefinition.Builder().name(name).type(type).dataFetcher { dataFetcher.invoke(it.getSource<T>(), ArgumentHolder(it.arguments)) }
            description?.let { field.description(description) }

            val argumentBuilder = ArgumentBuilder()
            argumentCreator.invoke(argumentBuilder)
            field.argument(argumentBuilder.arguments)

            fields.add(field.build())
        }
    }

    fun notNullA(name: String, type: GraphQLOutputType, description: String? = null, argumentCreator: (@DSL ArgumentBuilder).() -> Unit): (((@DSL T?).(ArgumentHolder) -> Any?)) -> Unit {
        return nullableA(name, GraphQLNonNull(type), description, argumentCreator)
    }

    fun nullable(name: String, type: GraphQLOutputType, description: String? = null, dataFetcher: (@DSL T).() -> Any?) {
        val field = GraphQLFieldDefinition.Builder().name(name).type(type).dataFetcher { dataFetcher.invoke(it.getSource<T>()) }
        description?.let { field.description(description) }
        fields.add(field.build())
    }

    fun notNull(name: String, type: GraphQLOutputType, description: String? = null, dataFetcher: (@DSL T).() -> Any?) {
        nullable(name, GraphQLNonNull(type), description, dataFetcher)
    }
}

class ArgumentBuilder {
    val arguments = mutableListOf<GraphQLArgument>()

    fun required(name: String, type: GraphQLInputType, description: String? = null) {
        optional(name, GraphQLNonNull(type), description)
    }

    fun optional(name: String, type: GraphQLInputType, description: String? = null, defaultValue: Any? = null) {
        val argument = GraphQLArgument.Builder().name(name).type(type)
        description?.let { argument.description(it) }
        defaultValue?.let { argument.defaultValue(it) }
        arguments.add(argument.build())
    }
}

class ArgumentHolder(private val args: Map<String, Any>) {
    @Suppress("UNCHECKED_CAST")
    operator fun <T> invoke(name: String): T {
        return args[name] as T
    }
}

fun <T> newObject(name: String, description: String? = null, fieldCreator: (@DSL FieldBuilder<T>).() -> Unit): GraphQLObjectType {
    val objectType = GraphQLObjectType.newObject().name(name)
    description?.let { objectType.description(it) }
    val fieldBuilder = FieldBuilder<T>()
    fieldCreator.invoke(fieldBuilder)
    objectType.fields(fieldBuilder.fields)
    return objectType.build()
}