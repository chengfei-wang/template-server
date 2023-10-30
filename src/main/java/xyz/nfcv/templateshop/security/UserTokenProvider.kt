package xyz.nfcv.templateshop.security

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.nfcv.templateshop.model.User
import java.util.*

@Component
class UserTokenProvider {
    @Autowired
    private lateinit var jwtManager: JwtManager


    fun verify(token: String): String? {
        try {
            val decoded = JWT.require(jwtManager.algorithm).build().verify(token)
            return decoded.subject
        } catch (e: JWTVerificationException) {
            e.printStackTrace()
        }
        return null
    }

    fun sign(user: User): String {
        return JWT.create()
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + LOGIN_TOKEN_EXPIRE))
            .withSubject(user.uid)
            .sign(jwtManager.algorithm)
    }


    companion object {
        const val LOGIN_TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000
    }
}