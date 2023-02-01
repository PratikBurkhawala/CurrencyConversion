package com.pratik.currencyconversion.di

import com.pratik.currencyconversion.data.db.Database
import com.pratik.currencyconversion.data.network.ApiService
import com.pratik.currencyconversion.data.source.RepositoryImpl
import com.pratik.currencyconversion.domain.repository.Repository
import com.pratik.currencyconversion.domain.utils.Constants
import com.pratik.currencyconversion.presentation.presenters.CurrencyConverterStore
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModules = module {

    single {
        HttpClient {
            followRedirects = false
            defaultRequest {
                url {
                    host = Constants.BASE_URL
                    url { protocol = URLProtocol.HTTPS }
                }
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                })
            }
        }
    }

    single { ApiService(httpClient = get()) }

    single { Database(sqlDriver = get(), backgroundDispatcher = Dispatchers.Default) }

    single<Repository> { RepositoryImpl(apiService = get(), database = get()) }

    single { CurrencyConverterStore(repository = get()) }
}

expect val platformModule: Module