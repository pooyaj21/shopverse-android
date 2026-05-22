package com.shopverse.android

import android.app.Application
import com.shopverse.core.domain.di.domainDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopVerseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopVerseApplication)
            // domainDiModule transitively wires data + service + preferences.
            modules(domainDiModule)
        }
    }
}
