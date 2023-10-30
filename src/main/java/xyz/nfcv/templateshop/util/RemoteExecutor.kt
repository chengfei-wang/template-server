package xyz.nfcv.templateshop.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.nfcv.templateshop.model.RemoteAction
import xyz.nfcv.templateshop.model.Responses
import xyz.nfcv.templateshop.security.RemoteControlProvider
import xyz.nfcv.templateshop.security.RemoteInfoProperties
import java.util.concurrent.TimeUnit

@Component
class RemoteExecutor {
    @Autowired
    lateinit var remoteInfo: RemoteInfoProperties

    @Autowired
    lateinit var remoteControlProvider: RemoteControlProvider

    inline fun <reified TypeResponse, reified TypeRequest> request(api: String, action: String, params: TypeRequest): Responses<TypeResponse> {
        val remoteAction = RemoteAction(remoteInfo.templateShopUrl, action)
        val token = remoteControlProvider.signServer(remoteAction)
        val remoteUrl = "${remoteInfo.templateUniversalUrl}/$api"
        val header = mapOf("Authorization" to token)
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val response = client.newCall(
            Request.Builder()
                .url(remoteUrl)
                .headers(header.toHeaders())
                .post(GSON.toJson(params).toRequestBody(JSON_MEDIA_TYPE))
                .build()
        ).execute()
        if (response.isSuccessful) {
            val body = response.body?.string() ?: return Responses.fail(message = "执行失败: 远程服务器异常")
            return GSON.fromJson(body, object : TypeToken<Responses<TypeResponse>>() {}.type)
        }
        return Responses.fail(message = "执行失败: 远程服务器无响应")
    }

    companion object {
        val GSON = Gson()
        val JSON_MEDIA_TYPE = "application/json;charset=utf-8".toMediaTypeOrNull()
    }
}