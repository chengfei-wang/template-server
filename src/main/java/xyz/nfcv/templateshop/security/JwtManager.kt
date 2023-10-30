package xyz.nfcv.templateshop.security

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JwtManager {
    @Autowired
    lateinit var rsaKeyFile: RsaKeyProperties

    val algorithm: Algorithm by lazy {
        Algorithm.RSA512(
            rsaKeyFile.publicKey.rsaPublicKey(),
            rsaKeyFile.privateKey.rsaPrivateKey()
        )
    }
}