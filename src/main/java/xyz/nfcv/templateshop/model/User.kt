package xyz.nfcv.templateshop.model

data class User(var uid: String, var name: String, var email: String, var password: String) {
    constructor() : this("", "", "", "")
}
