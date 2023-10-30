package xyz.nfcv.templateshop.model

data class UserSimple(var uid: String, var name: String, var email: String) {
    constructor() : this("", "", "")
}
