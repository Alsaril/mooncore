package live.luna

import graphql.GraphQL
import live.luna.auth.AuthHelper
import live.luna.auth.AuthInterceptor
import live.luna.graphql.Mutation
import live.luna.graphql.Query
import live.luna.graphql.annotations.buildSchema
import live.luna.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.PostConstruct

@SpringBootApplication
class Application
@Autowired
constructor(private val userService: UserService,
            private val authHelper: AuthHelper) {

    @PostConstruct
    fun init() {
        graphQL = GraphQL.newGraphQL(buildSchema(Query::class.java, Mutation::class.java)).build()
        live.luna.auth.userService = userService
        live.luna.auth.authHelper = authHelper
    }

    @Configuration
    @EnableWebMvc
    class WebMvcConfig : WebMvcConfigurer {
        override fun addInterceptors(registry: InterceptorRegistry) {
            registry.addInterceptor(AuthInterceptor())
        }
    }

    companion object {
        lateinit var graphQL: GraphQL

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}