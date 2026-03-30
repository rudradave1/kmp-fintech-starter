package com.rudradave.kmpfintechstarter.shared.domain.usecase

import app.cash.turbine.test
import com.rudradave.kmpfintechstarter.shared.testutil.FakeSyncMetadataStore
import com.rudradave.kmpfintechstarter.shared.testutil.FakeTransactionRepository
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class SyncTransactionsUseCaseTest {
    @Test
    fun `returns success and records sync metadata`() = runTest {
        val repository = FakeTransactionRepository().apply {
            syncResult = Result.success(Unit)
        }
        val metadataStore = FakeSyncMetadataStore()
        val useCase = SyncTransactionsUseCase(repository, metadataStore)

        val result = useCase()

        result.isSuccess shouldBe true
        metadataStore.observeLastSuccessfulSync().test {
            awaitItem().shouldNotBeNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `returns failure without recording metadata`() = runTest {
        val repository = FakeTransactionRepository().apply {
            syncResult = Result.failure(IllegalStateException("sync failed"))
        }
        val metadataStore = FakeSyncMetadataStore()
        val useCase = SyncTransactionsUseCase(repository, metadataStore)

        val result = useCase()

        result.isFailure shouldBe true
        metadataStore.observeLastSuccessfulSync().test {
            awaitItem() shouldBe null
            cancelAndIgnoreRemainingEvents()
        }
    }
}
