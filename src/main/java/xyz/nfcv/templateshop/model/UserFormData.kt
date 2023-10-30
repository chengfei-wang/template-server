package xyz.nfcv.templateshop.model

import java.util.*

data class UserFormData(
    val submit_id: String,
    val submit_ip_address: String,
    val submit_time: Date,
    val submit_user: String?,
    val submit_content: Map<String, String>,
)