package com.rudradave.kmpfintechstarter.shared.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.rudradave.kmpfintechstarter.shared.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path

private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
private val DARK_MODE = booleanPreferencesKey("dark_mode")

internal class UserPreferencesRepositoryImpl(
    producePath: () -> Path
) : UserPreferencesRepository {
    private val dataStore = PreferenceDataStoreFactory.createWithPath(
        produceFile = producePath
    )

    override fun isOnboardingCompleted(): Flow<Boolean> = dataStore.data.map { it[ONBOARDING_COMPLETED] ?: false }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { it[ONBOARDING_COMPLETED] = completed }
    }

    override fun isBiometricEnabled(): Flow<Boolean> = dataStore.data.map { it[BIOMETRIC_ENABLED] ?: false }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { it[BIOMETRIC_ENABLED] = enabled }
    }

    override fun isDarkMode(): Flow<Boolean> = dataStore.data.map { it[DARK_MODE] ?: true }

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE] = enabled }
    }
}
