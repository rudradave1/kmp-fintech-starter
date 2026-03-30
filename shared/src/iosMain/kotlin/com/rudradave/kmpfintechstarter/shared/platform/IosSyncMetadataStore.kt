package com.rudradave.kmpfintechstarter.shared.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSDate

internal class IosSyncMetadataStore : SyncMetadataStore {
    private val state = MutableStateFlow<Long?>(null)

    override fun observeLastSuccessfulSync(): Flow<Long?> = state.asStateFlow()

    override suspend fun setLastSuccessfulSync(timestamp: Long) {
        state.value = timestamp
    }
}

actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000.0).toLong()
