package com.pratik.currencyconversion.android.di

import com.pratik.currencyconversion.android.utils.SyncHelper
import org.koin.dsl.module

val appModule = module {
    single { SyncHelper() }
}