package com.pratik.currencyconversion.data.network

import com.goncalossilva.resources.Resource
import com.pratik.currencyconversion.domain.utils.Constants
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
var networkSuccess: Boolean = false

@ThreadLocal
var successResponse: Boolean = false

@ThreadLocal
var emptyRates: Boolean = false

@ThreadLocal
var noRates: Boolean = false

@ThreadLocal
var emptyResponse: Boolean = false

@ThreadLocal
var changeRequestUrl: Boolean = false

object MockServer {

    private val mockSuccessResponse200 = Resource(
        "src/commonTest/resources/success_response_200.json"
    ).readText()

    private val emptyRatesResponse200 = Resource(
        "src/commonTest/resources/empty_rates_response_200.json"
    ).readText()

    private val noRatesResponse200 = Resource(
        "src/commonTest/resources/no_rates_response_200.json"
    ).readText()

    private val errorResponse401 = Resource(
        "src/commonTest/resources/error_response_401.json"
    ).readText()

    private const val methodNotAllowedResponse = "Method not allowed"

    private const val emptyResponse200 = ""

    private const val contentTypeApplicationJson = "application/json"

    private const val contentTypeText = "text/plain"

    val mockServer = HttpClient(MockEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }

        engine {
            addHandler { request ->
                val endpoint = if (changeRequestUrl) {
                    Constants.ENDPOINT
                } else {
                    "/${Constants.ENDPOINT}"
                }
                when (request.url.encodedPath) {
                    endpoint -> {
                        var content = ""
                        var status: HttpStatusCode = HttpStatusCode.OK
                        var contentType: String = contentTypeApplicationJson
                        when {
                            networkSuccess && successResponse -> {
                                content = mockSuccessResponse200
                                status = HttpStatusCode.OK
                                contentType = contentTypeApplicationJson
                            }
                            networkSuccess && emptyRates -> {
                                content = emptyRatesResponse200
                                status = HttpStatusCode.OK
                                contentType = contentTypeApplicationJson
                            }
                            networkSuccess && noRates -> {
                                content = noRatesResponse200
                                status = HttpStatusCode.OK
                                contentType = contentTypeApplicationJson
                            }
                            networkSuccess && emptyResponse -> {
                                content = emptyResponse200
                                status = HttpStatusCode.OK
                                contentType = contentTypeApplicationJson
                            }
                            !networkSuccess -> {
                                content = errorResponse401
                                status = HttpStatusCode.Unauthorized
                                contentType = contentTypeApplicationJson
                            }
                        }

                        respond(
                            content = ByteReadChannel(content),
                            status = status,
                            headers = headersOf(HttpHeaders.ContentType, contentType)
                        )
                    }
                    else -> {
                        respond(
                            content = ByteReadChannel(methodNotAllowedResponse),
                            status = HttpStatusCode.MethodNotAllowed,
                            headers = headersOf(HttpHeaders.ContentType, contentTypeText)
                        )
                    }
                }
            }
        }
    }

    fun expectSuccess(isSuccess: Boolean = true) {
        networkSuccess = isSuccess
    }

    fun expectSuccessResponse(isSuccess: Boolean = true) {
        successResponse = isSuccess
    }

    fun expectEmptyRates(isSuccess: Boolean = true) {
        emptyRates = isSuccess
    }

    fun expectNoRates(isSuccess: Boolean = true) {
        noRates = isSuccess
    }

    fun expectEmptyResponse(isSuccess: Boolean = true) {
        emptyResponse = isSuccess
    }

    fun expectChangeRequestUrl(isSuccess: Boolean = true) {
        changeRequestUrl = isSuccess
    }


    fun reset() {
        networkSuccess = false
        successResponse = false
        emptyRates = false
        noRates = false
        emptyResponse = false
        changeRequestUrl = false
    }
}