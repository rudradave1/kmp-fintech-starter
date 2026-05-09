package com.rudradave.kmpfintechstarter.shared.platform

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.rudradave.kmpfintechstarter.shared.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.Executors

class AndroidSecurityManager(
    private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
) : SecurityManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private val _isLocked = MutableStateFlow(false)
    override val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    override val isSecurityEnabled: StateFlow<Boolean> = userPreferencesRepository
        .isBiometricEnabled()
        .stateIn(scope, SharingStarted.Eagerly, false)

    init {
        // Initially lock only if security was enabled previously
        if (isSecurityEnabled.value) {
            _isLocked.value = true
        }
    }

    override suspend fun unlock(): Boolean {
        // In a real app, you'd call this from an Activity and use BiometricPrompt
        // For this template, we'll simulate a successful unlock if biometrics are available
        return if (getBiometricStatus() == BiometricStatus.READY) {
            _isLocked.value = false
            true
        } else {
            // Fallback or error
            false
        }
    }

    override fun lock() {
        _isLocked.value = true
    }

    override fun getBiometricStatus(): BiometricStatus {
        val biometricManager = BiometricManager.from(context)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        return when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.READY
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.NOT_ENROLLED
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.NOT_AVAILABLE
            else -> BiometricStatus.NOT_AVAILABLE
        }
    }

    /** Helper to show biometric prompt - should be called from Activity. */
    fun showBiometricPrompt(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    _isLocked.value = false
                    activity.runOnUiThread { onSuccess() }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    activity.runOnUiThread { onError(errString.toString()) }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Fintech App")
            .setSubtitle("Log in using your biometric credential")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
