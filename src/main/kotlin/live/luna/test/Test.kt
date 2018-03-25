package live.luna.test

import graphql.ExecutionInput.newExecutionInput
import live.luna.Application.Companion.graphQL
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
class HelloController {

    @PostMapping("/")
    fun index(@RequestBody body: GraphQLRequest, request: HttpServletRequest): Any {

        val executionInput = newExecutionInput()
                .query(body.query)
                .variables(body.variables)

        val executionResult = graphQL.execute(executionInput.build())
        return executionResult.getData() ?: executionResult.errors
    }

    class GraphQLRequest {
        lateinit var query: String
        lateinit var variables: Map<String, Any>
    }
}