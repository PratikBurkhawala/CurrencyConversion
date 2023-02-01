package com.pratik.currencyconversion.data.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

internal actual fun createTestDriver(): SqlDriver {
    return NativeSqliteDriver(RatesDatabase.Schema, "")
}