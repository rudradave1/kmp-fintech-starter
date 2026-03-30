package com.rudradave.kmpfintechstarter.android.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.components.EmptyState
import com.rudradave.kmpfintechstarter.android.ui.components.FintechStatusBadge
import com.rudradave.kmpfintechstarter.android.ui.components.TransactionsLoadingState
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionDetailUiState
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionDetailViewModel
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionDetailRoute(
    transactionId: String,
    onBackClick: () -> Unit,
) {
    val koin = getKoin()
    val viewModel = remember(transactionId) {
        koin.get<TransactionDetailViewModel> { parametersOf(transactionId) }
    }
    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TransactionDetailScreen(
        state = uiState.value,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionDetailScreen(
    state: TransactionDetailUiState,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.transaction_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.transaction_detail_title),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            state.isLoading -> TransactionsLoadingState(modifier = Modifier.padding(innerPadding))
            state.transaction == null -> EmptyState(
                titleRes = R.string.error_state_title,
                bodyRes = R.string.transaction_detail_missing,
                modifier = Modifier.padding(innerPadding),
            )
            else -> {
                val transaction = requireNotNull(state.transaction)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(FintechDimens.screenPadding),
                    verticalArrangement = Arrangement.spacedBy(FintechDimens.sectionSpacing),
                ) {
                    TransactionHero(transaction = transaction)
                    TransactionDetailsCard(transaction = transaction)
                    StatusTimeline(transaction.status)
                }
            }
        }
    }
}

@Composable
private fun TransactionHero(transaction: Transaction) {
    Surface(
        shape = RoundedCornerShape(FintechDimens.cardCorner),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FintechDimens.cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(FintechDimens.mediumSpacing),
        ) {
            Surface(
                modifier = Modifier.size(FintechDimens.heroAvatar),
                shape = CircleShape,
                color = transaction.category.tint().copy(alpha = 0.18f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = transaction.merchantName.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = transaction.category.tint(),
                    )
                }
            }
            Text(
                text = transaction.merchantName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = transaction.formattedAmount(),
                style = MaterialTheme.typography.displaySmall,
                color = if (transaction.isDebit) MaterialTheme.colorScheme.error else transaction.category.tint(),
                textAlign = TextAlign.Center,
            )
            FintechStatusBadge(
                label = stringResource(transaction.status.labelRes()),
                tint = transaction.status.tint(),
            )
        }
    }
}

@Composable
private fun TransactionDetailsCard(transaction: Transaction) {
    Surface(
        shape = RoundedCornerShape(FintechDimens.cardCorner),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FintechDimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(FintechDimens.mediumSpacing),
        ) {
            DetailRow(
                label = stringResource(R.string.category_label),
                value = stringResource(transaction.category.labelRes()),
            )
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
            DetailRow(
                label = stringResource(R.string.timestamp_label),
                value = transaction.timestamp.formatDateTime(),
            )
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
            DetailRow(
                label = stringResource(R.string.reference_id_label),
                value = transaction.referenceId(),
                useMonospace = true,
            )
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
            DetailRow(
                label = stringResource(R.string.status_label),
                value = stringResource(transaction.status.labelRes()),
            )
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
            DetailRow(
                label = stringResource(R.string.type_label),
                value = stringResource(transaction.typeLabelRes()),
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    useMonospace: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = if (useMonospace) FontFamily.Monospace else null,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun StatusTimeline(status: TransactionStatus) {
    val timeline = when (status) {
        TransactionStatus.PENDING -> listOf(R.string.status_pending)
        TransactionStatus.COMPLETED -> listOf(R.string.status_pending, R.string.status_completed)
        TransactionStatus.FAILED -> listOf(R.string.status_pending, R.string.status_failed)
        TransactionStatus.REFUNDED -> listOf(
            R.string.status_pending,
            R.string.status_completed,
            R.string.status_refunded,
        )
    }
    Surface(
        shape = RoundedCornerShape(FintechDimens.cardCorner),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FintechDimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
        ) {
            Text(
                text = stringResource(R.string.status_timeline_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            timeline.forEachIndexed { index, titleRes ->
                TimelineNode(
                    title = stringResource(titleRes),
                    tint = status.tint(),
                    showConnector = index != timeline.lastIndex,
                )
            }
        }
    }
}

@Composable
private fun TimelineNode(
    title: String,
    tint: androidx.compose.ui.graphics.Color,
    showConnector: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(tint, CircleShape),
            )
            if (showConnector) {
                Box(
                    modifier = Modifier
                        .padding(top = FintechDimens.smallSpacing)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(FintechDimens.badgeCorner),
                        )
                        .height(FintechDimens.timelineSegmentHeight)
                        .width(FintechDimens.timelineSegmentWidth),
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF08101F)
@Composable
private fun TransactionDetailScreenPreview() {
    FintechStarterTheme {
        TransactionDetailScreen(
            state = TransactionDetailUiState(
                transaction = Transaction(
                    id = "transaction-1",
                    merchantName = "Myntra",
                    amount = 2499.0,
                    currency = "INR",
                    category = TransactionCategory.SHOPPING,
                    status = TransactionStatus.REFUNDED,
                    timestamp = System.currentTimeMillis(),
                    isDebit = false,
                ),
            ),
            onBackClick = {},
        )
    }
}
