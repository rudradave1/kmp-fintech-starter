package com.rudradave.kmpfintechstarter.shared.domain.repository

import com.rudradave.kmpfintechstarter.shared.domain.model.Account
import kotlinx.coroutines.flow.Flow

/** Contract for observing account data backed by a local cache. */
interface AccountRepository {
    /** Streams the locally cached account summary, if available. */
    fun getAccount(): Flow<Account?>

    /** Refreshes the account summary from the network and persists it locally. */
    suspend fun syncAccount(): Result<Unit>
}
