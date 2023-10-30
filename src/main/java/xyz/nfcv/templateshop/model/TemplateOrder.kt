package xyz.nfcv.templateshop.model

import java.util.*

data class TemplateOrder(var orderId: String, var template: TemplateShareNoContent, var buyer: UserSimple, var price: Int, var orderTime: Date) {
    constructor() : this("", TemplateShareNoContent(), UserSimple(), 0, Date())
}