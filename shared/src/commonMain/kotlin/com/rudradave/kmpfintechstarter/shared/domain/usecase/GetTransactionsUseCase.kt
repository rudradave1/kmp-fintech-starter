package com.rudradave.kmpfintechstarter.shared.domain.usecase

import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

/** Returns the full transaction stream from the local source of truth. */
class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
) {
    /** Invokes the use case. */
    operator fun invoke(): Flow<List<Transaction>> = transactionRepository.getTransactions()
}
