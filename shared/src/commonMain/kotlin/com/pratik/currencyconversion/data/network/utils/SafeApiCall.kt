package com.pratik.currencyconversion.data.network.utils

import com.pratik.currencyconversion.data.network.models.ErrorResponse
import com.pratik.currencyconversion.domain.utils.Constants
import io.ktor.client.plugins.*
import io.ktor.util.*
import io.ktor.util.network.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString

@InternalAPI
suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(data = apiCall.invoke())
    } catch (e: RedirectResponseException) { // 3xx errors
        val networkError = getError(responseContent = e.response.content)

        NetworkResult.Error(
            errorCode = networkError.status ?: e.response.status.value,
            errorMessage = networkError.message ?: e.message
        )
    } catch (e: ClientRequestException) { // 4xx errors
        val networkError = getError(responseContent = e.response.content)

        NetworkResult.Error(
            errorCode = networkError.status ?: e.response.status.value,
            errorMessage = networkError.message ?: e.message
        )
    } catch (e: ServerResponseException) { // 5xx errors
        val networkError = getError(responseContent = e.response.content)

        NetworkResult.Error(
            errorCode = networkError.status ?: e.response.status.value,
            errorMessage = networkError.message ?: e.message
        )
    } catch (e: UnresolvedAddressException) {
        NetworkResult.Error(
            errorCode = Constants.CONNECTIVITY_ERROR_CODE,
            errorMessage = Constants.CONNECTIVITY_ERROR
        )
    } catch (e: Exception) {
        NetworkResult.Error(
            errorCode = Constants.UNKNOWN_ERROR_CODE,
            errorMessage = Constants.UNKNOWN_ERROR
        )
    }
}

fun getError(responseContent: ByteReadChannel): ErrorResponse {
    return kotlinx.serialization.json.Json.decodeFromString(string = responseContent.toString())
}