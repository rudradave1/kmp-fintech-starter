package com.rudradave.kmpfintechstarter.shared.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class FintechApiService(
    private val httpClient: HttpClient,
    private val networkErrorMapper: NetworkErrorMapper,
) {
    suspend fun fetchTransactions(): NetworkResult<List<TransactionDto>> = safeApiCall {
        httpClient.get("transactions").body()
    }

    suspend fun fetchAccount(): NetworkResult<AccountDto> = safeApiCall {
        httpClient.get("account").body()
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): NetworkResult<T> {
        return try {
            NetworkResult.Success(block())
        } catch (throwable: Throwable) {
            networkErrorMapper.map(throwable)
        }
    }
}
