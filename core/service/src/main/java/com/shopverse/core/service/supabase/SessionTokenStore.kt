package com.shopverse.core.service.supabase

/**
 * Read/write access to the persisted Supabase session tokens.
 *
 * Declared here so [SupabaseTokenAuthenticator] can refresh the session at the
 * OkHttp layer; implemented in core:data on top of SharedPref, which owns the
 * actual storage keys.
 */
interface SessionTokenStore {
    val accessToken: String?
    val refreshToken: String?
    fun update(accessToken: String, refreshToken: String?)
    fun clear()
}
