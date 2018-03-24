package live.luna

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import live.luna.graphql_object.queryObject
import org.jetbrains.annotations.Nullable
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct


@SpringBootApplication
class Application {
    @PostConstruct
    fun init() {
        val schema = GraphQLSchema.newSchema().query(queryObject).build()
        graphQL = GraphQL.newGraphQL(schema).build()
    }

    companion object {
        @Nullable
        lateinit var graphQL: GraphQL

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}