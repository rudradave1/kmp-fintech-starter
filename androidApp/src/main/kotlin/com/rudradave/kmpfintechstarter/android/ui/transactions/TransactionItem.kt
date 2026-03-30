package com.rudradave.kmpfintechstarter.android.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(FintechDimens.cardCorner),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = FintechDimens.smallSpacing,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FintechDimens.cardPadding),
            horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(FintechDimens.cardCorner),
                    )
                    .padding(FintechDimens.mediumSpacing),
            ) {
                Icon(
                    imageVector = transaction.category.icon(),
                    contentDescription = stringResource(transaction.category.labelRes()),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(FintechDimens.smallSpacing),
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
                AssistChip(
                    onClick = {},
                    label = { Text(text = stringResource(transaction.status.labelRes())) },
                    leadingIcon = {
                        Icon(
                            imageVector = transaction.status.icon(),
                            contentDescription = null,
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        leadingIconContentColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
            Text(
                text = transaction.formattedAmount(),
                style = MaterialTheme.typography.titleMedium,
                color = if (transaction.isDebit) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
            )
        }
    }
}
