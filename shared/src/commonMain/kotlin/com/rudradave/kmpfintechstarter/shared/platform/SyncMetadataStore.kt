package com.rudradave.kmpfintechstarter.shared.platform

import kotlinx.coroutines.flow.Flow

/** Persists sync metadata using the best storage available on each platform. */
interface SyncMetadataStore {
    /** Streams the last successful sync timestamp in epoch milliseconds. */
    fun observeLastSuccessfulSync(): Flow<Long?>

    /** Persists the last successful sync timestamp in epoch milliseconds. */
    suspend fun setLastSuccessfulSync(timestamp: Long)
}

/** Returns the current wall-clock time in epoch milliseconds for the active platform. */
expect fun currentTimeMillis(): Long
