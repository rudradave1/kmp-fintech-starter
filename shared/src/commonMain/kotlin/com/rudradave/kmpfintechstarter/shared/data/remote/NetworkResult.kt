package com.rudradave.kmpfintechstarter.shared.data.remote

/** Result wrapper that keeps transport concerns inside the data layer. */
sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>
    data object Loading : NetworkResult<Nothing>
}
