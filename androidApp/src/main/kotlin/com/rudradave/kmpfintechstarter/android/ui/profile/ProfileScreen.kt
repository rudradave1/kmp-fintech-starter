package com.rudradave.kmpfintechstarter.android.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.platform.BiometricStatus
import com.rudradave.kmpfintechstarter.shared.presentation.ProfileUiState
import com.rudradave.kmpfintechstarter.shared.presentation.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenRoute() {
    val viewModel = koinInject<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        ProfileScreen(
            state = uiState,
            onToggleBiometric = viewModel::toggleBiometric,
            onToggleDarkMode = viewModel::toggleDarkMode,
            onActionClick = { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    onToggleBiometric: (Boolean) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(FintechDimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing)
    ) {
        item {
            ProfileHeader(state.userName, state.userEmail)
        }

        item {
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            val bioSubtitle = when (state.biometricStatus) {
                BiometricStatus.READY -> "Secure your app with fingerprint or face ID"
                BiometricStatus.NOT_ENROLLED -> "Security lock not enabled. Tap to setup."
                BiometricStatus.NOT_AVAILABLE -> "Biometrics not available on this device"
                BiometricStatus.SECURITY_UPDATE_REQUIRED -> "Security update required"
            }

            PreferenceItem(
                icon = Icons.Default.Fingerprint,
                title = "Biometric Login",
                subtitle = bioSubtitle,
                enabled = state.biometricStatus != BiometricStatus.NOT_AVAILABLE,
                onClick = {
                    if (state.biometricStatus == BiometricStatus.READY) {
                        onToggleBiometric(!state.isBiometricEnabled)
                    } else {
                        onActionClick("Please enable screen lock in your device settings.")
                    }
                },
                trailing = {
                    if (state.biometricStatus == BiometricStatus.READY) {
                        Switch(
                            checked = state.isBiometricEnabled, 
                            onCheckedChange = { onToggleBiometric(it) }
                        )
                    }
                }
            )

            PreferenceItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                subtitle = "Enable dark theme for the app",
                onClick = { onToggleDarkMode(!state.isDarkMode) },
                trailing = {
                    Switch(
                        checked = state.isDarkMode, 
                        onCheckedChange = { onToggleDarkMode(it) }
                    )
                }
            )
        }

        item {
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PreferenceItem(
                icon = Icons.Default.Person,
                title = "Personal Information",
                onClick = { onActionClick("Personal Information coming soon") }
            )
            PreferenceItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                onClick = { onActionClick("Notification settings coming soon") }
            )
            PreferenceItem(
                icon = Icons.Default.Help,
                title = "Help & Support",
                onClick = { onActionClick("Support center coming soon") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(FintechDimens.largeSpacing))
            Button(
                onClick = { onActionClick("Logout clicked") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Logout")
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Version ${state.appVersion}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(name: String, email: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FintechDimens.largeSpacing),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing)
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(8.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Column {
            Text(text = name, style = MaterialTheme.typography.headlineSmall)
            Text(text = email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreferenceItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val isClickable = onClick != null && enabled
    Surface(
        onClick = onClick ?: {},
        enabled = isClickable,
        shape = MaterialTheme.shapes.medium,
        color = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title, 
                    style = MaterialTheme.typography.titleSmall,
                    color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle, 
                        style = MaterialTheme.typography.bodySmall, 
                        color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
            }
            trailing?.invoke() ?: run {
                if (onClick != null) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}
