package com.shopverse.core.service.supabase

data class SupabaseConfig(
    val baseUrl: String,
    val anonKey: String,
) {
    val restUrl: String get() = "$baseUrl/rest/v1"
}
