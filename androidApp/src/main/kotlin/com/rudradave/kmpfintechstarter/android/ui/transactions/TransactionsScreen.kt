package com.rudradave.kmpfintechstarter.android.ui.transactions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.components.CategoryChip
import com.rudradave.kmpfintechstarter.android.ui.components.EmptyState
import com.rudradave.kmpfintechstarter.android.ui.components.TransactionsLoadingState
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionEvent
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionUiState
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionViewModel
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionsScreenRoute(onTransactionClick: (String) -> Unit) {
    val koin = getKoin()
    val viewModel = remember { koin.get<TransactionViewModel>() }
    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TransactionsScreen(
        state = uiState.value,
        onEvent = viewModel::onEvent,
        onTransactionClick = onTransactionClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsScreen(
    state: TransactionUiState,
    onEvent: (TransactionEvent) -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.transactions_title)) },
                actions = {
                    IconButton(
                        onClick = { onEvent(TransactionEvent.Refresh) },
                        enabled = !state.isRefreshing,
                    ) {
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
            state.isLoading -> TransactionsLoadingState(modifier = Modifier.padding(innerPadding))
            state.error != null && state.transactions.isEmpty() -> EmptyState(
                titleRes = R.string.error_state_title,
                bodyRes = R.string.error_state_body,
                modifier = Modifier.padding(innerPadding),
                onAction = { onEvent(TransactionEvent.Refresh) },
            )
            state.transactions.isEmpty() -> EmptyState(
                titleRes = R.string.empty_transactions_title,
                bodyRes = R.string.empty_transactions_body,
                modifier = Modifier.padding(innerPadding),
                actionLabelRes = R.string.action_clear_filter,
                onAction = if (state.selectedCategory != null) {
                    { onEvent(TransactionEvent.FilterByCategory(null)) }
                } else {
                    null
                },
            )
            else -> {
                val groupedTransactions = state.transactions
                    .groupBy { it.groupDate() }
                    .toSortedMap(compareByDescending { it })
                var listVisible by remember(state.transactions) { mutableStateOf(false) }
                LaunchedEffect(state.transactions) {
                    listVisible = false
                    listVisible = true
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = FintechDimens.sectionSpacing),
                    verticalArrangement = Arrangement.spacedBy(FintechDimens.itemSpacing),
                ) {
                    item {
                        CategoryFilters(
                            selected = state.selectedCategory,
                            onSelected = { category -> onEvent(TransactionEvent.FilterByCategory(category)) },
                        )
                    }
                    item {
                        AnimatedVisibility(
                            visible = listVisible,
                            enter = fadeIn(animationSpec = tween(durationMillis = 320)),
                            label = "transaction-list-load",
                        ) {
                            AnimatedContent(
                                targetState = state.selectedCategory,
                                transitionSpec = {
                                    (fadeIn(animationSpec = tween(220)) + slideInVertically { it / 10 }) togetherWith
                                        (fadeOut(animationSpec = tween(180)) + slideOutVertically { -it / 12 })
                                },
                                label = "transaction-filter-content",
                            ) {
                                TransactionGroupedContent(
                                    groupedTransactions = groupedTransactions,
                                    onTransactionClick = onTransactionClick,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionGroupedContent(
    groupedTransactions: Map<java.time.LocalDate, List<Transaction>>,
    onTransactionClick: (String) -> Unit,
) {
    androidx.compose.foundation.layout.Column(
        verticalArrangement = Arrangement.spacedBy(FintechDimens.itemSpacing),
    ) {
        groupedTransactions.forEach { (date, itemsForDate) ->
            val relativeHeader = date.relativeHeaderRes()
            Text(
                text = relativeHeader?.let { stringResource(it) } ?: date.formatMonthHeader(),
                modifier = Modifier.padding(horizontal = FintechDimens.screenPadding),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            androidx.compose.foundation.layout.Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                itemsForDate.forEach { transaction ->
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
private fun CategoryFilters(
    selected: TransactionCategory?,
    onSelected: (TransactionCategory?) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = FintechDimens.screenPadding, vertical = FintechDimens.smallSpacing),
        horizontalArrangement = Arrangement.spacedBy(FintechDimens.chipSpacing),
    ) {
        CategoryChip(
            label = stringResource(R.string.category_all),
            selected = selected == null,
            selectedColor = MaterialTheme.colorScheme.primary,
            onClick = { onSelected(null) },
        )
        TransactionCategory.entries
            .filterNot { it == TransactionCategory.OTHER }
            .forEach { category ->
                CategoryChip(
                    label = stringResource(category.labelRes()),
                    selected = selected == category,
                    selectedColor = category.tint(),
                    onClick = { onSelected(category) },
                )
            }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF08101F)
@Composable
private fun TransactionsScreenPreview() {
    FintechStarterTheme {
        TransactionsScreen(
            state = TransactionUiState(
                transactions = listOf(
                    previewTransaction("t1", "Swiggy", 347.0, TransactionCategory.FOOD),
                    previewTransaction("t2", "Uber", 234.0, TransactionCategory.TRANSPORT),
                    previewTransaction("t3", "Amazon", 1299.0, TransactionCategory.SHOPPING),
                    previewTransaction("t4", "Salary Credit", 73000.0, TransactionCategory.TRANSFER, false),
                ),
            ),
            onEvent = {},
            onTransactionClick = {},
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
