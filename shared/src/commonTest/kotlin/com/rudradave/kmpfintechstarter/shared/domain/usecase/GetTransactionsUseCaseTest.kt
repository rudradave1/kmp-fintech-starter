package com.rudradave.kmpfintechstarter.shared.domain.usecase

import app.cash.turbine.test
import com.rudradave.kmpfintechstarter.shared.testutil.FakeTransactionRepository
import com.rudradave.kmpfintechstarter.shared.testutil.SampleData
import io.kotest.matchers.collections.shouldContainExactly
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class GetTransactionsUseCaseTest {
    @Test
    fun `returns repository emissions in order`() = runTest {
        val repository = FakeTransactionRepository()
        val useCase = GetTransactionsUseCase(repository)
        val first = SampleData.transactions()
        val second = first.reversed()
        repository.emit(first)

        useCase().test {
            awaitItem().shouldContainExactly(first)

            repository.emit(second)
            awaitItem().shouldContainExactly(second)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
