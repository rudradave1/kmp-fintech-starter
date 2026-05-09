package com.rudradave.kmpfintechstarter.shared.platform

import kotlinx.coroutines.flow.StateFlow

/** Interface for managing app-level security, such as biometric authentication. */
interface SecurityManager {
    /** Whether the app is currently locked. */
    val isLocked: StateFlow<Boolean>

    /** Whether security is enabled by the user. */
    val isSecurityEnabled: StateFlow<Boolean>

    /** Attempts to unlock the app. */
    suspend fun unlock(): Boolean

    /** Locks the app. */
    fun lock()

    /** Returns the availability of biometric auth. */
    fun getBiometricStatus(): BiometricStatus
}

/** Possible states for biometric availability. */
enum class BiometricStatus {
    READY,
    NOT_AVAILABLE,
    NOT_ENROLLED,
    SECURITY_UPDATE_REQUIRED
}
