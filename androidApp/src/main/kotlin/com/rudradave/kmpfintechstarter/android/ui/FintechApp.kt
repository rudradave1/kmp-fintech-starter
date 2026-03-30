package com.rudradave.kmpfintechstarter.android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.dashboard.DashboardScreenRoute
import com.rudradave.kmpfintechstarter.android.ui.navigation.DashboardRoute
import com.rudradave.kmpfintechstarter.android.ui.navigation.TransactionDetailRoute
import com.rudradave.kmpfintechstarter.android.ui.navigation.TransactionsRoute
import com.rudradave.kmpfintechstarter.android.ui.transactions.TransactionDetailRoute as TransactionDetailScreenRoute
import com.rudradave.kmpfintechstarter.android.ui.transactions.TransactionsScreenRoute

/** Hosts the Android navigation shell for dashboard and transaction flows. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FintechApp() {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val destination = backStackEntry.value?.destination

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
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
                    icon = { Icon(Icons.Outlined.ReceiptLong, contentDescription = null) },
                    label = { Text(text = stringResource(R.string.transactions_title)) },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<DashboardRoute> {
                DashboardScreenRoute(
                    onTransactionClick = { navController.navigate(TransactionDetailRoute(it)) },
                )
            }
            composable<TransactionsRoute> {
                TransactionsScreenRoute(
                    onTransactionClick = { navController.navigate(TransactionDetailRoute(it)) },
                )
            }
            composable<TransactionDetailRoute> { backStackEntryForRoute ->
                val route = backStackEntryForRoute.toRoute<TransactionDetailRoute>()
                TransactionDetailScreenRoute(transactionId = route.id)
            }
        }
    }
}
