package live.luna.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AuthHelper {
    @Value("\${key}")
    lateinit var key: String

    fun generateToken(email: String): String {
        return Jwts.builder()
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact()
    }

    fun checkToken(token: String) = try {
        Jwts.parser().setSigningKey(key).parseClaimsJws(token).body.subject
    } catch (e: SignatureException) {
        null
    }
}