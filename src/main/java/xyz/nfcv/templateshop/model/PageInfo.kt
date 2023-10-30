package xyz.nfcv.templateshop.model

data class PageInfo(val pageId: String, val title: String, val elements: String, val deployType: String, val userVerify: String, val deployAddition: String) {
    constructor(): this("","", "", "", "", "")
}