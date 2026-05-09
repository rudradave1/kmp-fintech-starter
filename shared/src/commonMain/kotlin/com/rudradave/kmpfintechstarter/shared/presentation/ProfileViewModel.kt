package com.rudradave.kmpfintechstarter.shared.presentation

import com.rudradave.kmpfintechstarter.shared.domain.repository.UserPreferencesRepository
import com.rudradave.kmpfintechstarter.shared.platform.BiometricStatus
import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import com.rudradave.kmpfintechstarter.shared.platform.SecurityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val securityManager: SecurityManager,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dispatcherProvider: DispatcherProvider,
) : CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext = job + dispatcherProvider.main

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        userPreferencesRepository.isBiometricEnabled()
            .onEach { enabled ->
                _uiState.value = _uiState.value.copy(isBiometricEnabled = enabled)
            }.launchIn(this)

        userPreferencesRepository.isDarkMode()
            .onEach { enabled ->
                _uiState.value = _uiState.value.copy(isDarkMode = enabled)
            }.launchIn(this)
        
        _uiState.value = _uiState.value.copy(
            biometricStatus = securityManager.getBiometricStatus()
        )
    }

    fun toggleBiometric(enabled: Boolean) {
        launch(dispatcherProvider.io) {
            userPreferencesRepository.setBiometricEnabled(enabled)
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        launch(dispatcherProvider.io) {
            userPreferencesRepository.setDarkMode(enabled)
        }
    }

    fun clear() {
        cancel()
    }
}

data class ProfileUiState(
    val userName: String = "Neha Iyer",
    val userEmail: String = "neha.iyer@example.com",
    val isBiometricEnabled: Boolean = false,
    val biometricStatus: BiometricStatus = BiometricStatus.NOT_AVAILABLE,
    val isDarkMode: Boolean = true,
    val appVersion: String = "1.0.0 (Beta)"
)
