package com.rudradave.kmpfintechstarter.shared.domain.usecase

import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

/** Returns either all transactions or a category-specific filtered stream. */
class FilterTransactionsByCategoryUseCase(
    private val transactionRepository: TransactionRepository,
) {
    /** Invokes the use case. */
    operator fun invoke(category: TransactionCategory?): Flow<List<Transaction>> {
        return category?.let(transactionRepository::getTransactionsByCategory)
            ?: transactionRepository.getTransactions()
    }
}
