package live.luna

import graphql.GraphQL
import live.luna.graphql.Query
import live.luna.graphql.buildSchema
import live.luna.service.MasterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import javax.annotation.PostConstruct


@SpringBootApplication
class Application @Autowired constructor(masterService: MasterService) {

    init {
        live.luna.graphql.masterService = masterService
    }

    @PostConstruct
    fun init() {
        graphQL = GraphQL.newGraphQL(buildSchema(Query::class.java, null)).build()
    }

    companion object {
        lateinit var graphQL: GraphQL

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}