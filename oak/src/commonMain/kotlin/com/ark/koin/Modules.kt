package com.ark.koin

import com.ark.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformModule: Module

internal val sharedModule = module {

    single { HttpClientFactory.create(engine = get()) }

}