package com.shopverse.core.data.di

import com.shopverse.core.preferences.di.preferencesDiModule
import com.shopverse.core.service.di.serviceDiModule
import com.shopverse.core.shared.DefaultDispatcherProvider
import com.shopverse.core.shared.DispatcherProvider
import org.koin.dsl.module

val dataDiModule = module {
    includes(serviceDiModule, preferencesDiModule)

    single<DispatcherProvider> { DefaultDispatcherProvider() }
}
