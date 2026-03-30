package com.rudradave.kmpfintechstarter.shared.testutil

import com.rudradave.kmpfintechstarter.shared.platform.SyncMetadataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class FakeSyncMetadataStore : SyncMetadataStore {
    private val state = MutableStateFlow<Long?>(null)

    override fun observeLastSuccessfulSync(): Flow<Long?> = state.asStateFlow()

    override suspend fun setLastSuccessfulSync(timestamp: Long) {
        state.value = timestamp
    }
}
