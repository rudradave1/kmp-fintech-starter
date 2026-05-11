package com.rudradave.kmpfintechstarter.shared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rudradave.kmpfintechstarter.shared.domain.model.DashboardState
import com.rudradave.kmpfintechstarter.shared.presentation.DashboardUiState
import com.rudradave.kmpfintechstarter.shared.presentation.DashboardViewModel
import com.rudradave.kmpfintechstarter.shared.ui.components.*
import com.rudradave.kmpfintechstarter.shared.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.util.formatCurrency
import com.rudradave.kmpfintechstarter.shared.util.formattedAmountShared
import com.rudradave.kmpfintechstarter.shared.util.getLabel
import org.jetbrains.compose.resources.stringResource
import kmp_fintech_starter.shared.generated.resources.Res
import kmp_fintech_starter.shared.generated.resources.*

class DashboardScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<DashboardViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        DashboardContent(
            state = uiState,
            onRefresh = { viewModel.refresh() },
            onTransactionClick = { id -> navigator.push(TransactionDetailScreen(id)) },
            onSeeAllClick = { /* Navigate to Transactions Tab */ }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardContent(
    state: DashboardUiState,
    onRefresh: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.dashboard_title)) }
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading && !state.isRefreshing -> DashboardLoadingState(modifier = Modifier.padding(innerPadding))
            state.dashboardState == null -> EmptyState(
                title = stringResource(Res.string.recent_transactions_title),
                body = stringResource(Res.string.empty_dashboard_body),
                modifier = Modifier.padding(innerPadding),
                onAction = onRefresh,
                actionLabel = stringResource(Res.string.action_refresh)
            )
            else -> {
                val dashboard = requireNotNull(state.dashboardState)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
                ) {
                    item {
                        DashboardHeader(dashboard, onSeeAllClick)
                    }
                    items(dashboard.recentTransactions.take(5), key = { it.id }) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onClick = { onTransactionClick(transaction.id) },
                            categoryLabel = transaction.category.getLabel(),
                            statusLabel = transaction.status.getLabel(),
                            formattedAmount = transaction.formattedAmountShared(),
                            modifier = Modifier.padding(horizontal = FintechDimens.screenPadding),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader(
    dashboard: DashboardState,
    onSeeAllClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(
            horizontal = FintechDimens.screenPadding,
            vertical = FintechDimens.screenPadding,
        ),
        verticalArrangement = Arrangement.spacedBy(FintechDimens.sectionSpacing),
    ) {
        BankCard(
            bankName = stringResource(Res.string.account_bank_name),
            balanceLabel = stringResource(Res.string.account_card_balance_label),
            formattedBalance = formatCurrency(
                amount = dashboard.account.balance,
                currencyCode = dashboard.account.currency,
            ),
            maskedCardNumber = dashboard.account.maskedCardNumber,
            holderName = dashboard.account.holderName,
            networkLabel = stringResource(Res.string.account_card_network),
        )
        PromoCard(
            title = "Refer & Earn ₹500",
            description = "Invite your friends to Fintech Starter and get rewards.",
            actionLabel = "Invite Now",
            onAction = {}
        )
        QuickActions(modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
        ) {
            DashboardStatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.monthly_spend_label),
                value = formatCurrency(
                    amount = dashboard.monthlySpend,
                    currencyCode = dashboard.account.currency,
                ),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
            DashboardStatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.monthly_income_label),
                value = formatCurrency(
                    amount = dashboard.monthlyIncome,
                    currencyCode = dashboard.account.currency,
                ),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        SpendingBreakdown(
            spendingByCategory = dashboard.spendingByCategory,
            totalSpend = dashboard.monthlySpend,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(Res.string.recent_transactions_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = stringResource(Res.string.account_balance_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            OutlinedButton(onClick = onSeeAllClick) {
                Text(text = stringResource(Res.string.see_all_action))
            }
        }
    }
}

@Composable
private fun DashboardStatCard(
    label: String,
    value: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(FintechDimens.cardCorner),
        color = containerColor,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(FintechDimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor.copy(alpha = 0.70f),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = contentColor,
            )
        }
    }
}
