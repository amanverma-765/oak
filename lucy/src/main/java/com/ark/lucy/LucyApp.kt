package com.ark.lucy

import android.app.Application
import com.ark.koin.OakInitialiser
import com.ark.lucy.koin.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LucyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        OakInitialiser.initOak() {
            modules(sharedModule)
        }
    }
}