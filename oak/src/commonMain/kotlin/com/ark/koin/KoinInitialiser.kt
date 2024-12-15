package com.ark.koin

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object KoinInitialiser {
    fun initKoin(config: KoinAppDeclaration? = null) {
        startKoin {
            config?.invoke(this)
            modules(sharedModule, platformModule)
        }
    }
}