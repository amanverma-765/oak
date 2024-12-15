package com.ark.koin

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
    }