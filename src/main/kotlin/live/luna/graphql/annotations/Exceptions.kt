package live.luna.graphql.annotations

class GraphQLSchemaBuilderException(message: String, throwable: Throwable? = null) : Exception(message, throwable)

class GraphQLRuntimeException(message: String, throwable: Throwable? = null) : Exception(message, throwable)