package com.shopverse.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

fun provideEncryptedPrefs(context: Context): SharedPreferences {
    fun build(): SharedPreferences {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            context.packageName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    return try {
        build()
    } catch (e: Exception) {
        // A corrupted keyset can poison the keystore on reinstall / clear data.
        // Wipe the three known keyset files and rebuild from a fresh master key.
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .edit().clear().apply()
        context.getSharedPreferences(
            "androidx.security.crypto.master_key_keyset",
            Context.MODE_PRIVATE
        ).edit().clear().apply()
        context.getSharedPreferences(
            "androidx.security.crypto.keyset_prefs",
            Context.MODE_PRIVATE
        ).edit().clear().apply()
        build()
    }
}
