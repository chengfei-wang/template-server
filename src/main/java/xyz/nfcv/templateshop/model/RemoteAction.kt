package xyz.nfcv.templateshop.model

class RemoteAction(val server: String, private val action: String) {
    companion object {
        const val ACTION_DEPLOY = "DEPLOY"
        const val ACTION_ACCESS_LOG = "ACCESS_LOG"
        const val ACTION_FORM_DATA = "FORM_DATA"
    }

    val isDeploy: Boolean
        get() = action == ACTION_DEPLOY

    val isAccessLog: Boolean
        get() = action == ACTION_ACCESS_LOG

    val isFormData: Boolean
        get() = action == ACTION_FORM_DATA
}