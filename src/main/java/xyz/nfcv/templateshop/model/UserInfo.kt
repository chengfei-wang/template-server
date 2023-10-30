package xyz.nfcv.templateshop.model

data class UserInfo(var uid: String, var balance: Int) {
    constructor() : this("", 0)
}