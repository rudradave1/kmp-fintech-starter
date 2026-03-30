package com.rudradave.kmpfintechstarter.shared.domain.usecase

import com.rudradave.kmpfintechstarter.shared.domain.repository.TransactionRepository
import com.rudradave.kmpfintechstarter.shared.platform.SyncMetadataStore
import com.rudradave.kmpfintechstarter.shared.platform.currentTimeMillis

/** Triggers a transaction refresh and records successful sync metadata. */
class SyncTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
    private val syncMetadataStore: SyncMetadataStore,
) {
    /** Invokes the use case. */
    suspend operator fun invoke(): Result<Unit> {
        return transactionRepository.syncTransactions().onSuccess {
            syncMetadataStore.setLastSuccessfulSync(currentTimeMillis())
        }
    }
}
