package com.rudradave.kmpfintechstarter.shared.testutil

import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class FakeTransactionRepository : TransactionRepository {
    private val transactions = MutableStateFlow<List<Transaction>>(emptyList())
    var syncResult: Result<Unit> = Result.success(Unit)

    fun emit(list: List<Transaction>) {
        transactions.value = list
    }

    override fun getTransactions(): Flow<List<Transaction>> = transactions

    override fun getTransactionById(id: String): Flow<Transaction?> {
        return transactions.map { list -> list.firstOrNull { it.id == id } }
    }

    override suspend fun syncTransactions(): Result<Unit> = syncResult

    override fun getTransactionsByCategory(category: TransactionCategory): Flow<List<Transaction>> {
        return transactions.map { list -> list.filter { it.category == category } }
    }
}
