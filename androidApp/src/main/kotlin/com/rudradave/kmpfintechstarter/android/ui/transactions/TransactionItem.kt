package com.rudradave.kmpfintechstarter.android.ui.transactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudradave.kmpfintechstarter.android.ui.components.FintechStatusBadge
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus

/** Renders a production-style row for transaction history. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(FintechDimens.cardCorner)),
            onClick = onClick,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 88.dp)
                    .padding(horizontal = FintechDimens.screenPadding, vertical = FintechDimens.listRowPadding),
                horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier = Modifier.size(FintechDimens.iconCircle),
                    shape = CircleShape,
                    color = transaction.category.tint().copy(alpha = 0.18f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = transaction.category.icon(),
                            contentDescription = stringResource(transaction.category.labelRes()),
                            tint = transaction.category.tint(),
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = transaction.merchantName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(transaction.category.labelRes()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = transaction.formattedAmount(),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (transaction.isDebit) MaterialTheme.colorScheme.error else transaction.category.tint(),
                        textAlign = TextAlign.End,
                    )
                    FintechStatusBadge(
                        label = stringResource(transaction.status.labelRes()),
                        tint = transaction.status.tint(),
                    )
                }
            }
        }
        Divider(
            modifier = Modifier.padding(start = FintechDimens.dividerInset),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF08101F)
@Composable
private fun TransactionItemPreview() {
    FintechStarterTheme {
        TransactionItem(
            transaction = Transaction(
                id = "preview-transaction",
                merchantName = "Swiggy",
                amount = 347.0,
                currency = "INR",
                category = TransactionCategory.FOOD,
                status = TransactionStatus.COMPLETED,
                timestamp = System.currentTimeMillis(),
                isDebit = true,
            ),
            onClick = {},
        )
    }
}
