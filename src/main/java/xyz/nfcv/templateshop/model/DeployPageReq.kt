package xyz.nfcv.templateshop.model

class DeployPageReq(
    val template: String?,
    val deployType: String?,
    val userVerify: String?,
    val deployAddition: Array<String>?
)