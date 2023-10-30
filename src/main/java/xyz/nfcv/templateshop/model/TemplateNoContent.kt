package xyz.nfcv.templateshop.model

import java.util.*

data class TemplateNoContent(var tid: String, var creator: String, var title: String, var updateTime: Date) {
    constructor() : this("", "", "", Date())
}