package xyz.nfcv.templateshop.util

import okhttp3.internal.and
import java.security.NoSuchAlgorithmException
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object EncryptProvider {
    private const val salt = "c2d98bb6-e75d-4be7-85d1-1b1e740ae9c8"
    private fun sha256(str: String): String {
        val messageDigest: MessageDigest
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(str.toByteArray(StandardCharsets.UTF_8))
            return byte2Hex(messageDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun byte2Hex(bytes: ByteArray): String {
        val stringBuffer = StringBuilder()
        var temp: String
        for (byte in bytes) {
            temp = Integer.toHexString(byte and 0xFF)
            if (temp.length == 1) {
                stringBuffer.append("0")
            }
            stringBuffer.append(temp)
        }
        return stringBuffer.toString()
    }

    fun String.salted(id: String) = sha256(id + salt + this)
}