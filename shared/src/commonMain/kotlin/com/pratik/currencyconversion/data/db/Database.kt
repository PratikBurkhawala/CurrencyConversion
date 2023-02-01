package com.pratik.currencyconversion.data.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import compratikcurrencyconversion.data.db.Rates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.JsonObject
import kotlin.coroutines.CoroutineContext

class Database(
    sqlDriver: SqlDriver,
    private val backgroundDispatcher: CoroutineContext
) {
    private val database = RatesDatabase(sqlDriver)
    private val dbQuery = database.ratesDatabaseQueries

    internal suspend fun insertCurrencyDetails(rates: JsonObject) {
        if (rates.isNotEmpty()) {
            dbQuery.transactionWithContext(backgroundDispatcher) {
                dbQuery.clearTables()
                rates.entries.forEach { entry ->
                    val key = entry.key
                    val value = entry.value.toString().toDoubleOrNull()
                    if (value != null) {
                        dbQuery.insertRates(key, value)
                    }
                }
            }
        }
    }

    internal fun getAllRates(): Flow<List<Rates>> {
        return dbQuery.getAllRates().asFlow().mapToList().flowOn(backgroundDispatcher)
    }
}