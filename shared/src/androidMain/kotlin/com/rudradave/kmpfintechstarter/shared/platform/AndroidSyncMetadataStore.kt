package com.rudradave.kmpfintechstarter.shared.platform

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toOkioPath

private const val DATASTORE_FILE_NAME = "fintech_sync.preferences_pb"
private val LAST_SYNC_KEY = longPreferencesKey("last_successful_sync_epoch_ms")

internal class AndroidSyncMetadataStore(context: Context) : SyncMetadataStore {
    private val dataStore = PreferenceDataStoreFactory.createWithPath(
        produceFile = { context.preferencesDataStoreFile(DATASTORE_FILE_NAME).toOkioPath() },
    )

    override fun observeLastSuccessfulSync(): Flow<Long?> {
        return dataStore.data.map { preferences -> preferences[LAST_SYNC_KEY] }
    }

    override suspend fun setLastSuccessfulSync(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_SYNC_KEY] = timestamp
        }
    }
}

actual fun currentTimeMillis(): Long = System.currentTimeMillis()
