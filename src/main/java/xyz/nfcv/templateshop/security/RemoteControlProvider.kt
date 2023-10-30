package xyz.nfcv.templateshop.security

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTVerificationException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.nfcv.templateshop.model.RemoteAction

@Component
class RemoteControlProvider {
    @Autowired
    private lateinit var jwtManager: JwtManager

    fun verifyServer(token: String): RemoteAction? {
        try {
            val decoded = JWT.require(jwtManager.algorithm).build().verify(token)
            return GSON.fromJson(
                decoded.getClaim(REMOTE_ACTION_KEY).asString(),
                object : TypeToken<RemoteAction>() {}.type
            )
        } catch (e: JWTVerificationException) {
            e.printStackTrace()
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        return null
    }

    fun signServer(action: RemoteAction): String {
        return JWT.create()
            .withClaim(REMOTE_ACTION_KEY, GSON.toJson(action))
            .sign(jwtManager.algorithm)
    }

    companion object {
        private const val REMOTE_ACTION_KEY = "remote_action"
        private val GSON = Gson()
    }
}