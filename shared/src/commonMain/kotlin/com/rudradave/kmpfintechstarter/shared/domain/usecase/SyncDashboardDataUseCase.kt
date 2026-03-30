package com.rudradave.kmpfintechstarter.shared.domain.usecase

import com.rudradave.kmpfintechstarter.shared.domain.repository.AccountRepository
import com.rudradave.kmpfintechstarter.shared.platform.currentTimeMillis
import com.rudradave.kmpfintechstarter.shared.platform.SyncMetadataStore

/** Refreshes dashboard dependencies together and preserves last-sync metadata. */
class SyncDashboardDataUseCase(
    private val accountRepository: AccountRepository,
    private val syncTransactionsUseCase: SyncTransactionsUseCase,
    private val syncMetadataStore: SyncMetadataStore,
) {
    /** Invokes the use case. */
    suspend operator fun invoke(): Result<Unit> {
        val accountResult = accountRepository.syncAccount()
        val transactionResult = syncTransactionsUseCase()
        return if (accountResult.isSuccess && transactionResult.isSuccess) {
            syncMetadataStore.setLastSuccessfulSync(currentTimeMillis())
            Result.success(Unit)
        } else {
            accountResult.fold(
                onSuccess = { transactionResult },
                onFailure = { Result.failure(it) },
            )
        }
    }
}
