package com.shopverse.core.preferences.di

import com.shopverse.core.preferences.SharedPref
import com.shopverse.core.preferences.provideEncryptedPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesDiModule = module {
    single(createdAtStart = false) { provideEncryptedPrefs(androidContext()) }
    single { SharedPref(sharedPreferences = get()) }
}
