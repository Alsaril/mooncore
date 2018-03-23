package live.luna.graphql_object

import graphql.schema.*

@DslMarker
@Target(AnnotationTarget.TYPE)
annotation class DSL

class FieldBuilder<T> {
    val fields = mutableListOf<GraphQLFieldDefinition>()

    fun nullable(name: String, type: GraphQLOutputType, description: String? = null, argumentCreator: ((@DSL ArgumentBuilder<T>).() -> Unit)? = null): (((@DSL T).(ArgumentHolder) -> Any)) -> Unit {
        return { dataFetcher: T.(ArgumentHolder) -> Any ->
            val field = GraphQLFieldDefinition.Builder().name(name).type(type).dataFetcher { dataFetcher.invoke(it.getSource<T>(), ArgumentHolder(it.arguments)) }
            description?.let { field.description(description) }
            argumentCreator?.let {
                val argumentBuilder = ArgumentBuilder<T>()
                it.invoke(argumentBuilder)
                field.argument(argumentBuilder.arguments)
            }
            fields.add(field.build())
        }
    }

    fun notNull(name: String, type: GraphQLOutputType, description: String? = null, argumentCreator: ((@DSL ArgumentBuilder<T>).() -> Unit)? = null): (((@DSL T).(ArgumentHolder) -> Any)) -> Unit {
        return nullable(name, GraphQLNonNull(type), description, argumentCreator)
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

fun <T> newObject(name: String, description: String? = null, fieldCreator: (@DSL FieldBuilder<T>).() -> Unit): GraphQLOutputType {
    val objectType = GraphQLObjectType.newObject().name(name)
    description?.let { objectType.description(it) }
    val fieldBuilder = FieldBuilder<T>()
    fieldCreator.invoke(fieldBuilder)
    objectType.fields(fieldBuilder.fields)
    return objectType.build()
}