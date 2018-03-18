package live.luna

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.InputStreamReader
import java.io.Reader


@SpringBootApplication
class Application {
    companion object {
        val graphQL: GraphQL

        init {
            val schemaParser = SchemaParser()
            val schemaGenerator = SchemaGenerator()
            val typeRegistry = TypeDefinitionRegistry()
            typeRegistry.merge(schemaParser.parse(loadSchemaFile("schema.graphql")))

            val wiring = RuntimeWiring.newRuntimeWiring()
                    .type(newTypeWiring("Query")
                            .dataFetcher("hello") { it.getArgument("id") })
                    .type(newTypeWiring("Hello")
                            .dataFetcher("id") { it.getSource() }
                            .dataFetcher("text") { "123" })
                    .build()

            val schema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring)

            graphQL = GraphQL
                    .newGraphQL(schema)
                    .build()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }

        private fun loadSchemaFile(name: String): Reader {
            val stream = Application::class.java.classLoader.getResourceAsStream(name)
            return InputStreamReader(stream)
        }
    }
}