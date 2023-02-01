package com.pratik.currencyconversion.di

import com.pratik.currencyconversion.data.db.RatesDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(RatesDatabase.Schema, get(), "rates.db")
    }
}