package com.shopverse.core.service.supabase

import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class SupabaseClient(
    private val config: SupabaseConfig,
    private val httpClient: OkHttpClient,
) {

    suspend fun <T> get(
        path: String,
        query: Map<String, String> = emptyMap(),
        extraHeaders: Map<String, String> = emptyMap(),
        bearer: String? = null,
        parse: (body: String, headers: Headers) -> T,
    ): AppResult<T> = withContext(Dispatchers.IO) {
        val urlBuilder = "${config.restUrl}/${path.trimStart('/')}".toHttpUrl().newBuilder()
        query.forEach { (k, v) -> urlBuilder.addQueryParameter(k, v) }

        val requestBuilder = Request.Builder()
            .url(urlBuilder.build())
            .header("apikey", config.anonKey)
            .header("Authorization", "Bearer ${bearer ?: config.anonKey}")
            .header("Accept", "application/json")
        extraHeaders.forEach { (k, v) -> requestBuilder.header(k, v) }

        try {
            httpClient.newCall(requestBuilder.get().build()).execute().use { response ->
                val body = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    return@withContext AppResult.Error.Remote(
                        httpCode = response.code,
                        message = body.ifBlank { response.message },
                        cause = null,
                    )
                }
                AppResult.Success(parse(body, response.headers))
            }
        } catch (io: IOException) {
            AppResult.Error.Local(message = io.message, cause = io)
        } catch (t: Throwable) {
            AppResult.Error.Local(message = t.message, cause = t)
        }
    }
}
