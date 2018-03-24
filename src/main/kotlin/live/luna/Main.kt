package live.luna

import graphql.GraphQL
import live.luna.graphql_object.Query
import org.jetbrains.annotations.Nullable
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct


@SpringBootApplication
class Application {
    @PostConstruct
    fun init() {
        graphQL = GraphQL.newGraphQL(buildSchema(Query::class.java, null)).build()
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