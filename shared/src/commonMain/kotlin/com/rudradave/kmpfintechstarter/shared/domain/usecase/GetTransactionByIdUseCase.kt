package com.rudradave.kmpfintechstarter.shared.domain.usecase

import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

/** Returns a single transaction stream looked up from the local cache. */
class GetTransactionByIdUseCase(
    private val transactionRepository: TransactionRepository,
) {
    /** Invokes the use case. */
    operator fun invoke(id: String): Flow<Transaction?> = transactionRepository.getTransactionById(id)
}
