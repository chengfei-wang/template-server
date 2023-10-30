package xyz.nfcv.templateshop.model

import java.util.*

data class TemplateShare(var tid: String, var creator: String, var title: String, var content: String, var price: Int, var shareTime: Date) {
    constructor() : this("", "", "", "", 0, Date())
}