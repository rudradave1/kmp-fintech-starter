package com.rudradave.kmpfintechstarter.android.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.dashboard.DashboardScreenRoute
import com.rudradave.kmpfintechstarter.android.ui.navigation.DashboardRoute
import com.rudradave.kmpfintechstarter.android.ui.navigation.OnboardingRoute
import com.rudradave.kmpfintechstarter.android.ui.navigation.ProfileRoute
import com.rudradave.kmpfintechstarter.android.ui.navigation.TransactionDetailRoute
import com.rudradave.kmpfintechstarter.android.ui.navigation.TransactionsRoute
import com.rudradave.kmpfintechstarter.android.ui.onboarding.OnboardingScreen
import com.rudradave.kmpfintechstarter.android.ui.profile.ProfileScreenRoute
import com.rudradave.kmpfintechstarter.android.ui.security.LockScreen
import com.rudradave.kmpfintechstarter.android.ui.transactions.TransactionDetailRoute as TransactionDetailScreenRoute
import com.rudradave.kmpfintechstarter.android.ui.transactions.TransactionsScreenRoute
import com.rudradave.kmpfintechstarter.shared.platform.AndroidSecurityManager
import com.rudradave.kmpfintechstarter.shared.platform.SecurityManager
import org.koin.compose.koinInject

/** Hosts the Android navigation shell for dashboard and transaction flows. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FintechApp() {
    val securityManager = koinInject<SecurityManager>()
    val isLocked by securityManager.isLocked.collectAsStateWithLifecycle()
    val isSecurityEnabled by securityManager.isSecurityEnabled.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (isSecurityEnabled && isLocked) {
        LockScreen(
            onUnlock = {
                if (securityManager is AndroidSecurityManager && context is FragmentActivity) {
                    securityManager.showBiometricPrompt(
                        activity = context,
                        onSuccess = { /* isLocked will update automatically via flow */ },
                        onError = { /* show error toast or snackbar */ }
                    )
                } else {
                    // Fallback for non-biometric or other platforms
                    // For now, just mock unlock
                    (securityManager as? AndroidSecurityManager)?.let {
                        // In a real app, you might show a PIN screen here
                    }
                }
            }
        )
        return
    }

    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val destination = backStackEntry.value?.destination

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            val hideBottomBar = destination?.hasRoute<TransactionDetailRoute>() == true || 
                               destination?.hasRoute<OnboardingRoute>() == true
            if (!hideBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = destination?.hasRoute<DashboardRoute>() == true,
                        onClick = { navController.navigate(DashboardRoute) },
                        icon = { Icon(Icons.Outlined.AccountBalance, contentDescription = null) },
                        label = { Text(text = stringResource(R.string.dashboard_title)) },
                    )
                    NavigationBarItem(
                        selected = destination?.hasRoute<TransactionsRoute>() == true,
                        onClick = { navController.navigate(TransactionsRoute) },
                        icon = { Icon(Icons.AutoMirrored.Outlined.ReceiptLong, contentDescription = null) },
                        label = { Text(text = stringResource(R.string.transactions_title)) },
                    )
                    NavigationBarItem(
                        selected = destination?.hasRoute<ProfileRoute>() == true,
                        onClick = { navController.navigate(ProfileRoute) },
                        icon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                        label = { Text(text = "Profile") },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = OnboardingRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<OnboardingRoute> {
                OnboardingScreen(
                    onOnboardingFinished = {
                        navController.navigate(DashboardRoute) {
                            popUpTo(OnboardingRoute) { inclusive = true }
                        }
                    }
                )
            }
            composable<DashboardRoute>(
                enterTransition = { fadeIn(animationSpec = tween(220)) },
                exitTransition = { fadeOut(animationSpec = tween(180)) },
            ) {
                DashboardScreenRoute(
                    onTransactionClick = { navController.navigate(TransactionDetailRoute(it)) },
                    onSeeAllClick = { navController.navigate(TransactionsRoute) },
                )
            }
            composable<TransactionsRoute>(
                enterTransition = { fadeIn(animationSpec = tween(220)) + slideInHorizontally { it / 10 } },
                exitTransition = { fadeOut(animationSpec = tween(180)) + slideOutHorizontally { -it / 10 } },
            ) {
                TransactionsScreenRoute(
                    onTransactionClick = { navController.navigate(TransactionDetailRoute(it)) },
                )
            }
            composable<ProfileRoute>(
                enterTransition = { fadeIn(animationSpec = tween(220)) },
                exitTransition = { fadeOut(animationSpec = tween(180)) },
            ) {
                ProfileScreenRoute()
            }
            composable<TransactionDetailRoute>(
                enterTransition = { fadeIn(animationSpec = tween(220)) + slideInHorizontally { it / 6 } },
                exitTransition = { fadeOut(animationSpec = tween(180)) + slideOutHorizontally { it / 8 } },
            ) { backStackEntryForRoute ->
                val route = backStackEntryForRoute.toRoute<TransactionDetailRoute>()
                TransactionDetailScreenRoute(
                    transactionId = route.id,
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}
