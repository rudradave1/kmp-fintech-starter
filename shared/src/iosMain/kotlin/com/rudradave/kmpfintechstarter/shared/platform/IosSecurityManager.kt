package com.rudradave.kmpfintechstarter.shared.platform

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IosSecurityManager : SecurityManager {
    private val _isLocked = MutableStateFlow(false)
    override val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    override val isSecurityEnabled: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()

    override suspend fun unlock(): Boolean {
        // Mock iOS unlock
        _isLocked.value = false
        return true
    }

    override fun lock() {
        _isLocked.value = true
    }

    override fun getBiometricStatus(): BiometricStatus {
        return BiometricStatus.READY
    }
}
