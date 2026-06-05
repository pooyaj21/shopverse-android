package com.shopverse.core.data.auth

import com.shopverse.core.preferences.SharedPref
import com.shopverse.core.service.supabase.SessionTokenStore

private const val KEY_ACCESS_TOKEN = "auth_access_token"
private const val KEY_REFRESH_TOKEN = "auth_refresh_token"

class SessionTokenStoreImpl(
    private val sharedPref: SharedPref,
) : SessionTokenStore {

    override val accessToken: String?
        get() = sharedPref.read(KEY_ACCESS_TOKEN, null)

    override val refreshToken: String?
        get() = sharedPref.read(KEY_REFRESH_TOKEN, null)

    override fun update(accessToken: String, refreshToken: String?) {
        sharedPref.write(KEY_ACCESS_TOKEN, accessToken)
        if (refreshToken != null) sharedPref.write(KEY_REFRESH_TOKEN, refreshToken)
    }

    override fun clear() {
        sharedPref.remove(KEY_ACCESS_TOKEN)
        sharedPref.remove(KEY_REFRESH_TOKEN)
    }
}
