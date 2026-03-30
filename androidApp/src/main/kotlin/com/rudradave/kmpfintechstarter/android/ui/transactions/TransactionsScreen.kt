package com.rudradave.kmpfintechstarter.android.ui.transactions

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import com.rudradave.kmpfintechstarter.android.ui.components.EmptyState
import com.rudradave.kmpfintechstarter.android.ui.components.ScreenLoadingState
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
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
            state.isLoading -> ScreenLoadingState()
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
            )
            else -> {
                val grouped = state.transactions.groupBy { it.groupDate() }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = FintechDimens.sectionSpacing),
                    verticalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
                ) {
                    item {
                        CategoryFilters(
                            selected = state.selectedCategory,
                            onSelected = { category -> onEvent(TransactionEvent.FilterByCategory(category)) },
                        )
                    }
                    grouped.forEach { (date, itemsForDate) ->
                        item(key = date.toString()) {
                            Text(
                                text = date.formatForHeader(),
                                modifier = Modifier.padding(horizontal = FintechDimens.screenPadding),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        items(itemsForDate, key = { it.id }) { transaction ->
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryFilters(
    selected: TransactionCategory?,
    onSelected: (TransactionCategory?) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(FintechDimens.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(FintechDimens.chipSpacing),
    ) {
        FilterChip(
            selected = selected == null,
            onClick = { onSelected(null) },
            label = { Text(text = stringResource(R.string.category_all)) },
        )
        TransactionCategory.entries.forEach { category ->
            FilterChip(
                selected = selected == category,
                onClick = { onSelected(category) },
                label = { Text(text = stringResource(category.labelRes())) },
            )
        }
    }
}
