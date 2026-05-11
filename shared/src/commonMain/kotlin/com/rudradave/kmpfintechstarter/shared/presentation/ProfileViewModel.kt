package com.rudradave.kmpfintechstarter.shared.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.rudradave.kmpfintechstarter.shared.domain.repository.UserPreferencesRepository
import com.rudradave.kmpfintechstarter.shared.platform.BiometricStatus
import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import com.rudradave.kmpfintechstarter.shared.platform.SecurityManager
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
) : ScreenModel {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        userPreferencesRepository.isBiometricEnabled()
            .onEach { enabled ->
                _uiState.value = _uiState.value.copy(isBiometricEnabled = enabled)
            }.launchIn(screenModelScope)

        userPreferencesRepository.isDarkMode()
            .onEach { enabled ->
                _uiState.value = _uiState.value.copy(isDarkMode = enabled)
            }.launchIn(screenModelScope)
        
        _uiState.value = _uiState.value.copy(
            biometricStatus = securityManager.getBiometricStatus()
        )
    }

    fun toggleBiometric(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isBiometricEnabled = enabled)
        screenModelScope.launch {
            userPreferencesRepository.setBiometricEnabled(enabled)
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isDarkMode = enabled)
        screenModelScope.launch {
            userPreferencesRepository.setDarkMode(enabled)
        }
    }
}

data class ProfileUiState(
    val userName: String = "Neha Iyer",
    val userEmail: String = "neha.iyer@example.com",
    val isBiometricEnabled: Boolean = false,
    val biometricStatus: BiometricStatus = BiometricStatus.NOT_AVAILABLE,
    val isDarkMode: Boolean? = null,
    val appVersion: String = "1.0.0 (Beta)"
)
