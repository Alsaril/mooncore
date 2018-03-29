package live.luna.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import graphql.ExecutionInput.newExecutionInput
import live.luna.Application.Companion.graphQL
import live.luna.auth.CONTEXT_USER_ATTRIBUTE
import live.luna.entity.User
import live.luna.graphql.UserContext
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
class HelloController {

    @PostMapping("/api/graphql")
    fun index(@RequestBody body: GraphQLRequest, request: HttpServletRequest): Any {

        val executionInput = newExecutionInput()
                .query(body.query)
                .variables(body.variables)
                .context(UserContext(request.getAttribute(CONTEXT_USER_ATTRIBUTE) as User?))

        val executionResult = graphQL.execute(executionInput.build())
        return executionResult.getData() ?: executionResult.errors
    }

    class GraphQLRequest
    @JsonCreator constructor(
            @JsonProperty("query")
            val query: String,
            @JsonProperty("variables")
            val variables: Map<String, Any>
    )
}