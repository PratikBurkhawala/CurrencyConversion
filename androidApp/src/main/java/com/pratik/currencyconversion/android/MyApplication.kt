package com.pratik.currencyconversion.android

import android.app.Application
import com.pratik.currencyconversion.android.di.appModule
import com.pratik.currencyconversion.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(androidContext = this@MyApplication)
            appModule
        }
    }
}