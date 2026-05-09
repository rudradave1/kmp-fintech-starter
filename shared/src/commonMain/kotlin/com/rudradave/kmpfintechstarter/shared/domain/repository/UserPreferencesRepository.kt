package com.rudradave.kmpfintechstarter.shared.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
    fun isBiometricEnabled(): Flow<Boolean>
    suspend fun setBiometricEnabled(enabled: Boolean)
    fun isDarkMode(): Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
}
