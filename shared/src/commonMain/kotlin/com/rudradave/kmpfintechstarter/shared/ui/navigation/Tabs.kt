package com.rudradave.kmpfintechstarter.shared.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.rudradave.kmpfintechstarter.shared.ui.screens.DashboardScreen
import com.rudradave.kmpfintechstarter.shared.ui.screens.ProfileScreen
import com.rudradave.kmpfintechstarter.shared.ui.screens.TransactionsScreen

internal object DashboardTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.AccountBalance)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Dashboard",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(DashboardScreen())
    }
}

internal object TransactionsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.AutoMirrored.Outlined.ReceiptLong)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Transactions",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(TransactionsScreen())
    }
}

internal object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.Person)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Profile",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(ProfileScreen())
    }
}
