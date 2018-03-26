package live.luna.auth

import live.luna.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val CONTEXT_USER_ATTRIBUTE = "context_user_attribute"

@Volatile
lateinit var userService: UserService
@Volatile
lateinit var authHelper: AuthHelper

@Component
class AuthInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any?): Boolean {
        request.getHeader(HttpHeaders.AUTHORIZATION)
                ?.let { authHelper.checkToken(it) }
                ?.let { userService.getByEmail(it) }
                ?.let { request.setAttribute(CONTEXT_USER_ATTRIBUTE, it) }
        return true
    }
}