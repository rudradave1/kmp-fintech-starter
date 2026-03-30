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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.components.BankCard
import com.rudradave.kmpfintechstarter.android.ui.components.DashboardLoadingState
import com.rudradave.kmpfintechstarter.android.ui.components.EmptyState
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme
import com.rudradave.kmpfintechstarter.android.ui.transactions.TransactionItem
import com.rudradave.kmpfintechstarter.android.ui.transactions.formatCurrencyAmount
import com.rudradave.kmpfintechstarter.shared.domain.model.Account
import com.rudradave.kmpfintechstarter.shared.domain.model.DashboardState
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus
import com.rudradave.kmpfintechstarter.shared.presentation.DashboardUiState
import com.rudradave.kmpfintechstarter.shared.presentation.DashboardViewModel
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardScreenRoute(
    onTransactionClick: (String) -> Unit,
    onSeeAllClick: () -> Unit,
) {
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
        onSeeAllClick = onSeeAllClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(
    state: DashboardUiState,
    onRefresh: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onSeeAllClick: () -> Unit,
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
        when {
            state.isLoading -> DashboardLoadingState(modifier = Modifier.padding(innerPadding))
            state.dashboardState == null -> EmptyState(
                titleRes = R.string.recent_transactions_title,
                bodyRes = R.string.empty_dashboard_body,
                modifier = Modifier.padding(innerPadding),
                onAction = onRefresh,
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
                        Column(
                            modifier = Modifier.padding(
                                horizontal = FintechDimens.screenPadding,
                                vertical = FintechDimens.screenPadding,
                            ),
                            verticalArrangement = Arrangement.spacedBy(FintechDimens.sectionSpacing),
                        ) {
                            BankCard(
                                bankName = stringResource(R.string.account_bank_name),
                                balanceLabel = stringResource(R.string.account_card_balance_label),
                                formattedBalance = formatCurrencyAmount(
                                    amount = dashboard.account.balance,
                                    currencyCode = dashboard.account.currency,
                                ),
                                maskedCardNumber = dashboard.account.maskedCardNumber,
                                holderName = dashboard.account.holderName,
                                networkLabel = stringResource(R.string.account_card_network),
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
                            ) {
                                DashboardStatCard(
                                    modifier = Modifier.weight(1f),
                                    label = stringResource(R.string.monthly_spend_label),
                                    value = formatCurrencyAmount(
                                        amount = dashboard.monthlySpend,
                                        currencyCode = dashboard.account.currency,
                                    ),
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                )
                                DashboardStatCard(
                                    modifier = Modifier.weight(1f),
                                    label = stringResource(R.string.monthly_income_label),
                                    value = formatCurrencyAmount(
                                        amount = dashboard.monthlyIncome,
                                        currencyCode = dashboard.account.currency,
                                    ),
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = stringResource(R.string.recent_transactions_title),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )
                                    Text(
                                        text = stringResource(R.string.account_balance_label),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                OutlinedButton(onClick = onSeeAllClick) {
                                    Text(text = stringResource(R.string.see_all_action))
                                }
                            }
                        }
                    }
                    items(dashboard.recentTransactions.take(5), key = { it.id }) { transaction ->
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

@Preview(showBackground = true, backgroundColor = 0xFF08101F)
@Composable
private fun DashboardScreenPreview() {
    FintechStarterTheme {
        DashboardScreen(
            state = DashboardUiState(
                dashboardState = DashboardState(
                    account = Account(
                        id = "account-1",
                        holderName = "Neha Iyer",
                        balance = 124892.50,
                        currency = "INR",
                        maskedCardNumber = "•••• •••• •••• 1184",
                    ),
                    recentTransactions = listOf(
                        previewTransaction("swiggy", "Swiggy", 347.0, TransactionCategory.FOOD),
                        previewTransaction("uber", "Uber", 234.0, TransactionCategory.TRANSPORT),
                        previewTransaction("salary", "Salary Credit", 73000.0, TransactionCategory.TRANSFER, false),
                    ),
                    monthlySpend = 18325.5,
                    monthlyIncome = 88000.0,
                ),
            ),
            onRefresh = {},
            onTransactionClick = {},
            onSeeAllClick = {},
        )
    }
}

private fun previewTransaction(
    id: String,
    merchantName: String,
    amount: Double,
    category: TransactionCategory,
    isDebit: Boolean = true,
): Transaction {
    return Transaction(
        id = id,
        merchantName = merchantName,
        amount = amount,
        currency = "INR",
        category = category,
        status = TransactionStatus.COMPLETED,
        timestamp = System.currentTimeMillis(),
        isDebit = isDebit,
    )
}
