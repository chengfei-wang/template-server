package xyz.nfcv.templateshop.model

data class Responses<T>(val code: Int, val message: String, val data: T?) {
    companion object {
        fun <T> fail(code: Int = 400, message: String = "内部错误", data: T? = null) = Responses(code, message, data)
        fun <T> ok(code: Int = 200, message: String = "请求成功", data: T? = null) = Responses(code, message, data)
    }
}