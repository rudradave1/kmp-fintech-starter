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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.components.EmptyState
import com.rudradave.kmpfintechstarter.android.ui.components.ScreenLoadingState
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionDetailUiState
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionDetailViewModel
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionDetailRoute(transactionId: String) {
    val koin = getKoin()
    val viewModel = remember(transactionId) {
        koin.get<TransactionDetailViewModel> { parametersOf(transactionId) }
    }
    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TransactionDetailScreen(state = uiState.value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionDetailScreen(state: TransactionDetailUiState) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.transaction_detail_title)) }) },
    ) { innerPadding ->
        when {
            state.isLoading -> ScreenLoadingState()
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
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(FintechDimens.cardPadding),
                            verticalArrangement = Arrangement.spacedBy(FintechDimens.mediumSpacing),
                        ) {
                            Text(
                                text = transaction.merchantName,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = transaction.formattedAmount(),
                                style = MaterialTheme.typography.displaySmall,
                                color = if (transaction.isDebit) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            )
                            DetailRow(label = stringResource(R.string.reference_id_label), value = transaction.id)
                            DetailRow(label = stringResource(R.string.category_label), value = stringResource(transaction.category.labelRes()))
                            DetailRow(label = stringResource(R.string.status_label), value = stringResource(transaction.status.labelRes()))
                            DetailRow(label = stringResource(R.string.timestamp_label), value = transaction.timestamp.formatDateTime())
                        }
                    }
                    StatusTimeline(transaction.status)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(FintechDimens.smallSpacing)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
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
    Card {
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
                    showConnector = index != timeline.lastIndex,
                )
            }
        }
    }
}

@Composable
private fun TimelineNode(title: String, showConnector: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(FintechDimens.smallSpacing),
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
                        .padding(horizontal = FintechDimens.timelineSegmentWidth),
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
