package xyz.nfcv.templateshop.model

import java.util.*

data class Template(var tid: String, var creator: String, var title: String, var content: String, var updateTime: Date) {
    constructor() : this("", "", "", "", Date())
}