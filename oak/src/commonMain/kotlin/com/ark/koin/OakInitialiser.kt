package com.ark.koin

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object OakInitialiser {
    fun initOak(config: KoinAppDeclaration? = null) {
        startKoin {
            config?.invoke(this)
            modules(sharedModule, platformModule)
        }
    }
}