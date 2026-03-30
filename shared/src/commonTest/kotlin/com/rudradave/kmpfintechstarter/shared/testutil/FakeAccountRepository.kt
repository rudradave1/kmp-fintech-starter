package com.rudradave.kmpfintechstarter.shared.testutil

import com.rudradave.kmpfintechstarter.shared.domain.model.Account
import com.rudradave.kmpfintechstarter.shared.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeAccountRepository : AccountRepository {
    private val account = MutableStateFlow<Account?>(null)
    var syncResult: Result<Unit> = Result.success(Unit)

    fun emit(value: Account?) {
        account.value = value
    }

    override fun getAccount(): Flow<Account?> = account

    override suspend fun syncAccount(): Result<Unit> = syncResult
}
