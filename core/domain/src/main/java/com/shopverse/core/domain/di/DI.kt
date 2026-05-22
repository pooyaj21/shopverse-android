package com.shopverse.core.domain.di

import com.shopverse.core.data.di.dataDiModule
import org.koin.dsl.module

val domainDiModule = module {
    includes(dataDiModule)
}
