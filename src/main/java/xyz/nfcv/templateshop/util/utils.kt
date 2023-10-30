package xyz.nfcv.templateshop.util

import java.io.File
import java.util.*

fun File.child(name: String): File = File(this, name)

fun checkEmpty(vararg fields: String?): Boolean {
    return fields.any { it.isNullOrEmpty() }
}

fun checkEmail(email: String?): Boolean {
    return email != null && Regex("^(.+)@(.+)\$").matches(email)
}

fun uuid() = UUID.randomUUID().toString()