package com.rudradave.kmpfintechstarter.shared.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.rudradave.kmpfintechstarter.shared.data.local.toDomainAccount
import com.rudradave.kmpfintechstarter.shared.data.local.db.FintechDatabase
import com.rudradave.kmpfintechstarter.shared.data.remote.FintechApiService
import com.rudradave.kmpfintechstarter.shared.data.remote.NetworkResult
import com.rudradave.kmpfintechstarter.shared.domain.model.Account
import com.rudradave.kmpfintechstarter.shared.domain.repository.AccountRepository
import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import com.rudradave.kmpfintechstarter.shared.platform.currentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AccountRepositoryImpl(
    private val database: FintechDatabase,
    private val apiService: FintechApiService,
    private val dispatcherProvider: DispatcherProvider,
) : AccountRepository {
    override fun getAccount(): Flow<Account?> {
        return database.fintechDatabaseQueries
            .selectAccount()
            .asFlow()
            .mapToOneOrNull(dispatcherProvider.io)
            .map { accountEntity -> accountEntity?.toDomainAccount() }
    }

    override suspend fun syncAccount(): Result<Unit> {
        return when (val result = apiService.fetchAccount()) {
            is NetworkResult.Success -> {
                val account = result.data
                database.fintechDatabaseQueries.upsertAccount(
                    id = account.id,
                    holderName = account.holderName,
                    balance = account.balance,
                    currency = account.currency,
                    maskedCardNumber = account.maskedCardNumber,
                    updatedAt = currentTimeMillis(),
                )
                Result.success(Unit)
            }
            is NetworkResult.Error -> Result.failure(IllegalStateException(result.message))
            NetworkResult.Loading -> Result.success(Unit)
        }
    }
}
