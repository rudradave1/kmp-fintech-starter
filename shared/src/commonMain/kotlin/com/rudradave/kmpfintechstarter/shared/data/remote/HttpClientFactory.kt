package com.rudradave.kmpfintechstarter.shared.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "65f07f44ead08fa78a50f929.mockapi.io"

internal fun createHttpClient(engineFactory: HttpClientEngineFactory<*>): HttpClient {
    return HttpClient(engineFactory) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                },
            )
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 15_000
            requestTimeoutMillis = 30_000
        }
        install(HttpRequestRetry) {
            maxRetries = 3
            exponentialDelay()
            retryOnExceptionOrServerErrors(maxRetries)
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.INFO
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_URL
                encodedPathSegments = listOf("api", "v1")
            }
            contentType(ContentType.Application.Json)
        }
    }
}
