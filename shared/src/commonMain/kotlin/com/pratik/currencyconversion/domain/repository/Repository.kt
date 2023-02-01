package com.pratik.currencyconversion.domain.repository

import com.pratik.currencyconversion.domain.models.CurrencyRate
import com.pratik.currencyconversion.domain.models.RepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonObject

interface Repository {
    suspend fun getAllRates(): Flow<RepositoryResult<List<CurrencyRate>>>
    suspend fun refresh()
}