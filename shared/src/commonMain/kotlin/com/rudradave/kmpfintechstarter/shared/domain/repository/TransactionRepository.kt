package com.rudradave.kmpfintechstarter.shared.domain.repository

import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow

/** Contract for reading and syncing transaction data in an offline-first manner. */
interface TransactionRepository {
    /** Streams all locally cached transactions ordered for presentation. */
    fun getTransactions(): Flow<List<Transaction>>

    /** Streams a single locally cached transaction by identifier. */
    fun getTransactionById(id: String): Flow<Transaction?>

    /** Refreshes transactions from the network and persists them locally. */
    suspend fun syncTransactions(): Result<Unit>

    /** Streams locally cached transactions for a specific category. */
    fun getTransactionsByCategory(category: TransactionCategory): Flow<List<Transaction>>
}
