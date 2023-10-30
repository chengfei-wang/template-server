package xyz.nfcv.templateshop.model

import java.util.*

data class TemplateShareNoContent(var tid: String, var creator: String, var title: String, var price: Int, var shareTime: Date) {
    constructor() : this("", "", "", 0, Date())
}