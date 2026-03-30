package com.rudradave.kmpfintechstarter.shared.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.rudradave.kmpfintechstarter.shared.data.local.normalizedDto
import com.rudradave.kmpfintechstarter.shared.data.local.toDomainTransaction
import com.rudradave.kmpfintechstarter.shared.data.local.db.FintechDatabase
import com.rudradave.kmpfintechstarter.shared.data.remote.FintechApiService
import com.rudradave.kmpfintechstarter.shared.data.remote.NetworkResult
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.repository.TransactionRepository
import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class TransactionRepositoryImpl(
    private val database: FintechDatabase,
    private val apiService: FintechApiService,
    private val dispatcherProvider: DispatcherProvider,
) : TransactionRepository {
    override fun getTransactions(): Flow<List<Transaction>> {
        return database.fintechDatabaseQueries
            .selectAllTransactions()
            .asFlow()
            .mapToList(dispatcherProvider.io)
            .map { rows -> rows.map { entity -> entity.toDomainTransaction() } }
    }

    override fun getTransactionById(id: String): Flow<Transaction?> {
        return database.fintechDatabaseQueries
            .selectTransactionById(id)
            .asFlow()
            .mapToOneOrNull(dispatcherProvider.io)
            .map { entity -> entity?.toDomainTransaction() }
    }

    override suspend fun syncTransactions(): Result<Unit> {
        return when (val result = apiService.fetchTransactions()) {
            is NetworkResult.Success -> {
                database.fintechDatabaseQueries.transaction {
                    database.fintechDatabaseQueries.deleteAllTransactions()
                    result.data.forEach { dto ->
                        val transaction = dto.normalizedDto()
                        database.fintechDatabaseQueries.upsertTransaction(
                            id = transaction.id,
                            merchantName = transaction.merchantName,
                            amount = transaction.amount,
                            currency = transaction.currency,
                            category = transaction.category,
                            status = transaction.status,
                            timestamp = transaction.timestamp,
                            isDebit = if (transaction.isDebit) 1L else 0L,
                        )
                    }
                }
                Result.success(Unit)
            }
            is NetworkResult.Error -> Result.failure(IllegalStateException(result.message))
            NetworkResult.Loading -> Result.success(Unit)
        }
    }

    override fun getTransactionsByCategory(category: TransactionCategory): Flow<List<Transaction>> {
        return database.fintechDatabaseQueries
            .selectTransactionsByCategory(category.name)
            .asFlow()
            .mapToList(dispatcherProvider.io)
            .map { rows -> rows.map { entity -> entity.toDomainTransaction() } }
    }
}
