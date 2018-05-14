package live.luna.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import graphql.ExecutionInput.newExecutionInput
import live.luna.Application.Companion.graphQL
import live.luna.auth.CONTEXT_USER_ATTRIBUTE
import live.luna.entity.User
import live.luna.graphql.UserContext
import org.springframework.http.HttpHeaders.USER_AGENT
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.servlet.http.HttpServletRequest


@RestController
class MainController {

    @PostMapping("/api/graphql")
    fun index(@RequestBody body: GraphQLRequest, request: HttpServletRequest): Any {

        val executionInput = newExecutionInput()
                .query(body.query)
                .variables(body.variables)
                .context(UserContext(request.getAttribute(CONTEXT_USER_ATTRIBUTE) as User?))

        val executionResult = graphQL.execute(executionInput.build())
        return Wrapper(executionResult.getData() ?: executionResult.errors)
    }

    class GraphQLRequest
    @JsonCreator constructor(
            @JsonProperty("query")
            val query: String,
            @JsonProperty("variables")
            val variables: Map<String, Any>?
    )

    class Wrapper(val data: Any)

    var redirected = ""

    @GetMapping("/vk_auth")
    fun vkAuth(request: HttpServletRequest, attributes: RedirectAttributes): RedirectView {


        val redirectUri = "${request.remoteHost}:${request.serverPort}/vk_auth_confirmation"

        redirected = redirectUri

        val url = "https://oauth.vk.com/authorize?" +
                "client_id=$CLIENT_ID&" +
                "display=page&" +
                "redirect_uri=$redirectUri" +
                "&scope=email&" +
                "response_type=code&" +
                "v=5.74"

        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView")
        attributes.addAttribute("attribute", "redirectWithRedirectView")
        return RedirectView(url)
    }

    @GetMapping("/vk_auth_confirmation")
    fun vkAuthConfirmation(request: HttpServletRequest): Any {
        val code = request.getParameter("code") ?: throw IllegalArgumentException("GET-parameter \'code\' not found!")

        val url = "https://oauth.vk.com/access_token?" +
                "client_id=$CLIENT_ID&" +
                "client_secret=$CLIENT_SECRET&" +
                "redirect_uri=http://127.0.0.1:8081&" +
                "code=$code"


        val obj = URL(url)
        val con = obj.openConnection() as HttpURLConnection

        // optional default is GET
        con.setRequestMethod("GET")

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT)

        val responseCode = con.getResponseCode()
        System.out.println("\nSending 'GET' request to URL : $url")
        println("Response Code : $responseCode")

        val `in` = BufferedReader(
                InputStreamReader(con.getInputStream()))
        var inputLine: String? = `in`.readLine()
        val response = StringBuffer()

        while (inputLine != null) {
            response.append(inputLine)
            inputLine = `in`.readLine()
        }
        `in`.close()

        //print result
        val result = response.toString()

        return "kek"
    }


    companion object {
        private const val CLIENT_ID = "6478856"
        private const val CLIENT_SECRET = "dSyz828mAq951PmbUsbP"
    }
}