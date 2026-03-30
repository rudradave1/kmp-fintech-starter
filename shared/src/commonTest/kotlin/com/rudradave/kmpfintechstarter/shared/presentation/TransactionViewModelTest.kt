package com.rudradave.kmpfintechstarter.shared.presentation

import app.cash.turbine.test
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.usecase.FilterTransactionsByCategoryUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.GetTransactionsUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.SyncTransactionsUseCase
import com.rudradave.kmpfintechstarter.shared.testutil.FakeSyncMetadataStore
import com.rudradave.kmpfintechstarter.shared.testutil.FakeTransactionRepository
import com.rudradave.kmpfintechstarter.shared.testutil.SampleData
import com.rudradave.kmpfintechstarter.shared.testutil.TestDispatcherProvider
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

class TransactionViewModelTest {
    @Test
    fun `emits loaded transactions and responds to category filtering`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = FakeTransactionRepository().apply { emit(SampleData.transactions()) }
        val viewModel = TransactionViewModel(
            getTransactions = GetTransactionsUseCase(repository),
            syncTransactions = SyncTransactionsUseCase(repository, FakeSyncMetadataStore()),
            filterByCategory = FilterTransactionsByCategoryUseCase(repository),
            dispatcherProvider = TestDispatcherProvider(dispatcher),
        )

        viewModel.uiState.test {
            dispatcher.scheduler.advanceUntilIdle()
            awaitItem()
            awaitItem().transactions.shouldContainExactly(SampleData.transactions())

            viewModel.onEvent(TransactionEvent.FilterByCategory(TransactionCategory.FOOD))
            dispatcher.scheduler.advanceUntilIdle()
            awaitItem()
            val filtered = awaitItem()
            filtered.selectedCategory shouldBe TransactionCategory.FOOD
            filtered.transactions.shouldContainExactly(
                listOf(SampleData.transactions().first { it.category == TransactionCategory.FOOD }),
            )
            viewModel.clear()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits error when refresh fails`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = FakeTransactionRepository().apply {
            emit(SampleData.transactions())
            syncResult = Result.failure(IllegalStateException("refresh failed"))
        }
        val viewModel = TransactionViewModel(
            getTransactions = GetTransactionsUseCase(repository),
            syncTransactions = SyncTransactionsUseCase(repository, FakeSyncMetadataStore()),
            filterByCategory = FilterTransactionsByCategoryUseCase(repository),
            dispatcherProvider = TestDispatcherProvider(dispatcher),
        )

        viewModel.uiState.test {
            dispatcher.scheduler.advanceUntilIdle()
            awaitItem()
            awaitItem()

            viewModel.onEvent(TransactionEvent.Refresh)
            dispatcher.scheduler.advanceUntilIdle()
            awaitItem()
            val refreshed = awaitItem()
            refreshed.error shouldBe "refresh failed"
            refreshed.isRefreshing shouldBe false
            viewModel.clear()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
