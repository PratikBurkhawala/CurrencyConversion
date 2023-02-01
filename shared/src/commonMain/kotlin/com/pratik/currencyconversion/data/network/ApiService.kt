package com.pratik.currencyconversion.data.network

import com.pratik.currencyconversion.data.network.models.SuccessResponse
import com.pratik.currencyconversion.domain.utils.Constants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ApiService constructor(private val httpClient: HttpClient) {

    suspend fun fetchLatestCurrencyRates(): SuccessResponse = try {
        httpClient.get(urlString = Constants.ENDPOINT, block = {
            parameter("app_id", Constants.APP_ID)
        }).body()
    } catch (e: Exception) {
        throw e
    }
}