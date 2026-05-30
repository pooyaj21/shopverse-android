package com.shopverse.core.service.supabase

import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

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
    ): AppResult<T> = execute(
        baseUrl = config.restUrl,
        path = path,
        query = query,
        extraHeaders = extraHeaders,
        bearer = bearer,
        body = null,
        method = HttpMethod.GET,
        parse = parse,
    )

    suspend fun <T> authPost(
        path: String,
        query: Map<String, String> = emptyMap(),
        body: String,
        bearer: String? = null,
        parse: (body: String, headers: Headers) -> T,
    ): AppResult<T> = execute(
        baseUrl = config.authUrl,
        path = path,
        query = query,
        extraHeaders = emptyMap(),
        bearer = bearer,
        body = body,
        method = HttpMethod.POST,
        parse = parse,
    )

    private suspend fun <T> execute(
        baseUrl: String,
        path: String,
        query: Map<String, String>,
        extraHeaders: Map<String, String>,
        bearer: String?,
        body: String?,
        method: HttpMethod,
        parse: (body: String, headers: Headers) -> T,
    ): AppResult<T> = withContext(Dispatchers.IO) {
        val urlBuilder = "$baseUrl/${path.trimStart('/')}".toHttpUrl().newBuilder()
        query.forEach { (k, v) -> urlBuilder.addQueryParameter(k, v) }

        val requestBuilder = Request.Builder()
            .url(urlBuilder.build())
            .header("apikey", config.anonKey)
            .header("Authorization", "Bearer ${bearer ?: config.anonKey}")
            .header("Accept", "application/json")
        extraHeaders.forEach { (k, v) -> requestBuilder.header(k, v) }

        val request = when (method) {
            HttpMethod.GET -> requestBuilder.get().build()
            HttpMethod.POST -> requestBuilder
                .post((body ?: "").toRequestBody(JSON_MEDIA_TYPE))
                .build()
        }

        try {
            httpClient.newCall(request).execute().use { response ->
                val responseBody = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    return@withContext AppResult.Error.Remote(
                        httpCode = response.code,
                        message = responseBody.ifBlank { response.message },
                        cause = null,
                    )
                }
                AppResult.Success(parse(responseBody, response.headers))
            }
        } catch (io: IOException) {
            AppResult.Error.Local(message = io.message, cause = io)
        } catch (t: Throwable) {
            AppResult.Error.Local(message = t.message, cause = t)
        }
    }

    private enum class HttpMethod { GET, POST }
}
