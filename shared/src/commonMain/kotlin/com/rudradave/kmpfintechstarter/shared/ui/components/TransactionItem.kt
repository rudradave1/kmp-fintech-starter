package com.rudradave.kmpfintechstarter.shared.ui.components

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus
import com.rudradave.kmpfintechstarter.shared.ui.components.FintechStatusBadge
import com.rudradave.kmpfintechstarter.shared.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.ui.theme.FintechStarterTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    categoryLabel: String,
    statusLabel: String,
    formattedAmount: String,
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
                    .heightIn(min = 96.dp)
                    .padding(horizontal = FintechDimens.screenPadding, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    modifier = Modifier.size(FintechDimens.iconCircle),
                    shape = CircleShape,
                    color = getCategoryColor(transaction.category).copy(alpha = 0.18f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = getCategoryEmoji(transaction.category),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = transaction.merchantName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = categoryLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = formattedAmount,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = if (transaction.isDebit) MaterialTheme.colorScheme.error else getCategoryColor(transaction.category),
                        textAlign = TextAlign.End,
                    )
                    FintechStatusBadge(
                        label = statusLabel,
                        tint = getStatusColor(transaction.status),
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(start = FintechDimens.dividerInset),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f),
        )
    }
}

// Helper functions for UI mapping in commonMain
private fun getCategoryColor(category: TransactionCategory) = when (category) {
    TransactionCategory.FOOD -> androidx.compose.ui.graphics.Color(0xFFFFA54D)
    TransactionCategory.TRANSPORT -> androidx.compose.ui.graphics.Color(0xFF5DA9FF)
    TransactionCategory.SHOPPING -> androidx.compose.ui.graphics.Color(0xFFFF78C7)
    TransactionCategory.ENTERTAINMENT -> androidx.compose.ui.graphics.Color(0xFF9D7BFF)
    TransactionCategory.UTILITIES -> androidx.compose.ui.graphics.Color(0xFF24C5B2)
    TransactionCategory.HEALTH -> androidx.compose.ui.graphics.Color(0xFFFF6F91)
    TransactionCategory.TRANSFER -> androidx.compose.ui.graphics.Color(0xFF43C97B)
    TransactionCategory.OTHER -> androidx.compose.ui.graphics.Color(0xFFB8C0D9)
}

private fun getCategoryEmoji(category: TransactionCategory) = when (category) {
    TransactionCategory.FOOD -> "🍔"
    TransactionCategory.TRANSPORT -> "🚗"
    TransactionCategory.SHOPPING -> "🛍️"
    TransactionCategory.ENTERTAINMENT -> "🎬"
    TransactionCategory.UTILITIES -> "⚡"
    TransactionCategory.HEALTH -> "🏥"
    TransactionCategory.TRANSFER -> "💸"
    TransactionCategory.OTHER -> "📦"
}

private fun getStatusColor(status: TransactionStatus) = when (status) {
    TransactionStatus.PENDING -> androidx.compose.ui.graphics.Color(0xFFFFC857)
    TransactionStatus.COMPLETED -> androidx.compose.ui.graphics.Color(0xFF4FCB81)
    TransactionStatus.FAILED -> androidx.compose.ui.graphics.Color(0xFFFF6B6B)
    TransactionStatus.REFUNDED -> androidx.compose.ui.graphics.Color(0xFF63A8FF)
}
