package com.rudradave.kmpfintechstarter.shared.data.remote

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.utils.io.errors.IOException

internal class NetworkErrorMapper {
    fun map(throwable: Throwable): NetworkResult.Error {
        return when (throwable) {
            is ClientRequestException -> NetworkResult.Error(
                message = throwable.message ?: "Client request failed",
                code = throwable.response.status.value,
            )
            is ServerResponseException -> NetworkResult.Error(
                message = throwable.message ?: "Server request failed",
                code = throwable.response.status.value,
            )
            is RedirectResponseException -> NetworkResult.Error(
                message = throwable.message ?: "Redirect request failed",
                code = throwable.response.status.value,
            )
            is ResponseException -> NetworkResult.Error(
                message = throwable.message ?: "Response failed",
                code = throwable.response.status.value,
            )
            is IOException -> NetworkResult.Error(message = throwable.message ?: "Network unavailable")
            else -> NetworkResult.Error(message = throwable.message ?: "Unexpected network error")
        }
    }
}
