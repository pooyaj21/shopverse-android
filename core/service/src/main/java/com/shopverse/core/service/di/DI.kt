package com.shopverse.core.service.di

import com.shopverse.core.service.api.ProductService
import com.shopverse.core.service.api.ProductServiceImpl
import com.shopverse.core.service.supabase.SupabaseClient
import com.shopverse.core.service.supabase.SupabaseConfig
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val serviceDiModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            )
            .build()
    }

    // SupabaseConfig is provided by the app module from BuildConfig at startup.
    single { SupabaseClient(config = get<SupabaseConfig>(), httpClient = get()) }

    single<ProductService> { ProductServiceImpl(client = get(), json = get()) }
}
