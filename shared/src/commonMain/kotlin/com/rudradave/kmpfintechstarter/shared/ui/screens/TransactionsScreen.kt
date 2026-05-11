package com.rudradave.kmpfintechstarter.shared.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionEvent
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionUiState
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionViewModel
import com.rudradave.kmpfintechstarter.shared.ui.components.CategoryChip
import com.rudradave.kmpfintechstarter.shared.ui.components.EmptyState
import com.rudradave.kmpfintechstarter.shared.ui.components.TransactionsLoadingState
import com.rudradave.kmpfintechstarter.shared.ui.components.TransactionItem
import com.rudradave.kmpfintechstarter.shared.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.util.formattedAmountShared
import com.rudradave.kmpfintechstarter.shared.util.getLabel
import org.jetbrains.compose.resources.stringResource
import kmp_fintech_starter.shared.generated.resources.Res
import kmp_fintech_starter.shared.generated.resources.*

class TransactionsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<TransactionViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        TransactionsContent(
            state = uiState,
            onEvent = viewModel::onEvent,
            onTransactionClick = { id -> navigator.push(TransactionDetailScreen(id)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsContent(
    state: TransactionUiState,
    onEvent: (TransactionEvent) -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.transactions_title)) },
                actions = {
                    IconButton(
                        onClick = { onEvent(TransactionEvent.Refresh) },
                        enabled = !state.isRefreshing,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = stringResource(Res.string.action_refresh),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            state.isLoading -> TransactionsLoadingState(modifier = Modifier.padding(innerPadding))
            state.error != null && state.transactions.isEmpty() -> EmptyState(
                title = stringResource(Res.string.error_state_title),
                body = stringResource(Res.string.error_state_body),
                modifier = Modifier.padding(innerPadding),
                onAction = { onEvent(TransactionEvent.Refresh) },
                actionLabel = stringResource(Res.string.action_refresh)
            )
            state.transactions.isEmpty() -> EmptyState(
                title = stringResource(Res.string.empty_transactions_title),
                body = stringResource(Res.string.empty_transactions_body),
                modifier = Modifier.padding(innerPadding),
                actionLabel = if (state.selectedCategory != null) stringResource(Res.string.action_clear_filter) else null,
                onAction = if (state.selectedCategory != null) {
                    { onEvent(TransactionEvent.FilterByCategory(null)) }
                } else {
                    null
                },
            )
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = FintechDimens.sectionSpacing),
                    verticalArrangement = Arrangement.spacedBy(FintechDimens.itemSpacing),
                ) {
                    item {
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { onEvent(TransactionEvent.Search(it)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = FintechDimens.screenPadding),
                            placeholder = { Text("Search transactions...") },
                            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                        )
                    }
                    item {
                        CategoryFilters(
                            selected = state.selectedCategory,
                            onSelected = { category -> onEvent(TransactionEvent.FilterByCategory(category)) },
                        )
                    }
                    item {
                        TransactionGroupedContent(
                            transactions = state.transactions,
                            onTransactionClick = onTransactionClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionGroupedContent(
    transactions: List<Transaction>,
    onTransactionClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(FintechDimens.itemSpacing),
        modifier = Modifier.padding(horizontal = FintechDimens.screenPadding)
    ) {
        transactions.forEach { transaction ->
            TransactionItem(
                transaction = transaction,
                onClick = { onTransactionClick(transaction.id) },
                categoryLabel = transaction.category.getLabel(),
                statusLabel = transaction.status.getLabel(),
                formattedAmount = transaction.formattedAmountShared()
            )
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
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(FintechDimens.chipSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(FintechDimens.screenPadding))
        CategoryChip(
            label = stringResource(Res.string.category_all),
            selected = selected == null,
            selectedColor = MaterialTheme.colorScheme.primary,
            onClick = { onSelected(null) },
        )
        TransactionCategory.entries
            .filterNot { it == TransactionCategory.OTHER }
            .forEach { category ->
                CategoryChip(
                    label = category.getLabel(),
                    selected = selected == category,
                    selectedColor = MaterialTheme.colorScheme.primary,
                    onClick = { onSelected(category) },
                )
            }
        Spacer(modifier = Modifier.width(FintechDimens.screenPadding))
    }
}
