package com.rudradave.kmpfintechstarter.shared.domain.usecase

import app.cash.turbine.test
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.testutil.FakeTransactionRepository
import com.rudradave.kmpfintechstarter.shared.testutil.SampleData
import io.kotest.matchers.collections.shouldContainExactly
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class FilterTransactionsByCategoryUseCaseTest {
    @Test
    fun `filters transactions by the selected category`() = runTest {
        val repository = FakeTransactionRepository().apply { emit(SampleData.transactions()) }
        val useCase = FilterTransactionsByCategoryUseCase(repository)

        useCase(TransactionCategory.FOOD).test {
            awaitItem().shouldContainExactly(
                listOf(SampleData.transactions().first { it.category == TransactionCategory.FOOD }),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
