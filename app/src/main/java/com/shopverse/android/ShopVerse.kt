package com.shopverse.android

import android.app.Application
import com.shopverse.android.core.extension.applyNightMode
import com.shopverse.android.presentation.appDiModule
import com.shopverse.core.domain.di.domainDiModule
import com.shopverse.core.domain.theme.GetThemeModeUseCase
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShopVerse : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShopVerse)
            // domainDiModule transitively wires data + service + preferences.
            modules(domainDiModule, appDiModule)
        }
        get<GetThemeModeUseCase>()().applyNightMode()
    }
}
