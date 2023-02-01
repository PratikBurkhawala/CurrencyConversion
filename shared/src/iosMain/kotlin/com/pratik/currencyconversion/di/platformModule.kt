package com.pratik.currencyconversion.di

import com.pratik.currencyconversion.data.db.RatesDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(RatesDatabase.Schema, "rates.db")
    }
}