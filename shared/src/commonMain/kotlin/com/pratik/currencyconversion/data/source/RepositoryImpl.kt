package com.pratik.currencyconversion.data.source

import com.pratik.currencyconversion.data.db.Database
import com.pratik.currencyconversion.data.network.ApiService
import com.pratik.currencyconversion.data.network.utils.NetworkResult
import com.pratik.currencyconversion.data.network.utils.safeApiCall
import com.pratik.currencyconversion.domain.mappers.rateDbToCurrencyRate
import com.pratik.currencyconversion.domain.models.CurrencyRate
import com.pratik.currencyconversion.domain.models.RepositoryResult
import com.pratik.currencyconversion.domain.repository.Repository
import com.pratik.currencyconversion.domain.utils.Constants.EMPTY_LIST_MESSAGE
import compratikcurrencyconversion.data.db.Rates
import io.ktor.client.plugins.logging.*
import io.ktor.util.*
import io.ktor.util.logging.*
import kotlinx.coroutines.flow.*

class RepositoryImpl(
    private val apiService: ApiService,
    private val database: Database
) : Repository {

    @OptIn(InternalAPI::class)
    override suspend fun getAllRates(): Flow<RepositoryResult<List<CurrencyRate>>> = channelFlow {
        database.getAllRates().collectLatest { list ->
            if (list.isEmpty()) {
                val response = safeApiCall {
                    apiService.fetchLatestCurrencyRates()
                }
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.rates?.let { database.insertCurrencyDetails(it) }
                        val newList = database.getAllRates().firstOrNull()
                        if (newList.isNullOrEmpty()) {
                            send(RepositoryResult.Error(EMPTY_LIST_MESSAGE))
                        } else {
                            send(RepositoryResult.Success(convertRatesToCurrencyRateModel(newList)))
                        }
                    }
                    is NetworkResult.Error -> {
                        send(RepositoryResult.Error(response.errorMessage))
                    }
                }
            } else {
                send(RepositoryResult.Success(convertRatesToCurrencyRateModel(list)))
            }
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun refresh() {
        val response = safeApiCall {
            apiService.fetchLatestCurrencyRates()
        }
        if (response.data?.rates != null) {
            database.insertCurrencyDetails(response.data.rates)
        }
    }

    private fun convertRatesToCurrencyRateModel(rates: List<Rates>): List<CurrencyRate> {
        return rates.map { it.rateDbToCurrencyRate() }
    }
}