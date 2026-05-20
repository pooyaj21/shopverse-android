package com.shopverse.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopVerseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopVerseApplication)
            // modules(...) — wired in once :core:domain exposes its Koin module
        }
    }
}
