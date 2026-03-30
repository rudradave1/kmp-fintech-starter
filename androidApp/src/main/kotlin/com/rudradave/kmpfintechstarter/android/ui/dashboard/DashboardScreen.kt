package com.rudradave.kmpfintechstarter.android.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.components.ScreenLoadingState
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.android.ui.transactions.TransactionItem
import com.rudradave.kmpfintechstarter.android.ui.transactions.formatCurrencyAmount
import com.rudradave.kmpfintechstarter.shared.presentation.DashboardUiState
import com.rudradave.kmpfintechstarter.shared.presentation.DashboardViewModel
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardScreenRoute(onTransactionClick: (String) -> Unit) {
    val koin = getKoin()
    val viewModel = remember { koin.get<DashboardViewModel>() }
    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    DashboardScreen(
        state = uiState.value,
        onRefresh = viewModel::refresh,
        onTransactionClick = onTransactionClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(
    state: DashboardUiState,
    onRefresh: () -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.dashboard_title)) },
                actions = {
                    IconButton(onClick = onRefresh, enabled = !state.isRefreshing) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = stringResource(R.string.action_refresh),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (state.isLoading) {
            ScreenLoadingState()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(FintechDimens.sectionSpacing),
            ) {
                item {
                    val dashboard = state.dashboardState
                    if (dashboard != null) {
                        Column(
                            modifier = Modifier.padding(FintechDimens.screenPadding),
                            verticalArrangement = Arrangement.spacedBy(FintechDimens.sectionSpacing),
                        ) {
                            AccountCard(
                                balance = dashboard.account.balance,
                                currency = dashboard.account.currency,
                                holderName = dashboard.account.holderName,
                                maskedCardNumber = dashboard.account.maskedCardNumber,
                            )
                            MonthlySummaryCard(
                                spend = dashboard.monthlySpend,
                                income = dashboard.monthlyIncome,
                                currency = dashboard.account.currency,
                            )
                            Text(
                                text = stringResource(R.string.recent_transactions_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
                items(state.dashboardState?.recentTransactions.orEmpty(), key = { it.id }) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onClick = { onTransactionClick(transaction.id) },
                        modifier = Modifier.padding(horizontal = FintechDimens.screenPadding),
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountCard(
    balance: Double,
    currency: String,
    holderName: String,
    maskedCardNumber: String,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FintechDimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(FintechDimens.mediumSpacing),
        ) {
            Text(
                text = stringResource(R.string.account_balance_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = formatCurrencyAmount(balance, currency),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = holderName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = maskedCardNumber,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun MonthlySummaryCard(spend: Double, income: Double, currency: String) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FintechDimens.cardPadding),
            horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
        ) {
            SummaryMetric(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.monthly_spend_label),
                value = formatCurrencyAmount(spend, currency),
            )
            SummaryMetric(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.monthly_income_label),
                value = formatCurrencyAmount(income, currency),
            )
        }
    }
}

@Composable
private fun SummaryMetric(modifier: Modifier, label: String, value: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FintechDimens.smallSpacing),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
