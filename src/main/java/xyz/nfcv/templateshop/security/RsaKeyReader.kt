package xyz.nfcv.templateshop.security

import org.springframework.core.io.ClassPathResource
import java.io.File
import java.io.FileOutputStream
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec


fun String.rsaPublicKey(): RSAPublicKey {
    ClassPathResource(this).inputStream.use { stream ->
        return KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(stream.readBytes())) as RSAPublicKey
    }
}

fun String.rsaPrivateKey(): RSAPrivateKey {
    ClassPathResource(this).inputStream.use { stream ->
        return KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(stream.readBytes())) as RSAPrivateKey
    }
}

private fun generateRSAKeyPairs(keysDir: File) {
    val kpg = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }
    val keyPair = kpg.generateKeyPair()
    val public: Key = keyPair.public
    val private: Key = keyPair.private
    FileOutputStream(File(keysDir, "jwt_rsa.key")).use { out -> out.write(private.encoded) }
    FileOutputStream(File(keysDir, "jwt_rsa.pub")).use { out -> out.write(public.encoded) }
}