package com.rudradave.kmpfintechstarter.android.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory

@Composable
fun SpendingBreakdown(
    spendingByCategory: Map<TransactionCategory, Double>,
    totalSpend: Double,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = FintechDimens.mediumSpacing),
        verticalArrangement = Arrangement.spacedBy(FintechDimens.mediumSpacing)
    ) {
        Text(
            text = "Spending Breakdown",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing)
        ) {
            DonutChart(
                spendingByCategory = spendingByCategory,
                totalSpend = totalSpend,
                modifier = Modifier.size(120.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(FintechDimens.smallSpacing)
            ) {
                spendingByCategory.entries.sortedByDescending { it.value }.take(4).forEach { (category, amount) ->
                    val percentage = (amount / totalSpend).toFloat()
                    CategorySpendingRow(
                        category = category,
                        amount = amount,
                        percentage = percentage
                    )
                }
            }
        }
    }
}

@Composable
private fun DonutChart(
    spendingByCategory: Map<TransactionCategory, Double>,
    totalSpend: Double,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        var startAngle = -90f
        spendingByCategory.entries.sortedByDescending { it.value }.forEach { (category, amount) ->
            val sweepAngle = (amount / totalSpend).toFloat() * 360f
            drawArc(
                color = getCategoryColor(category),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 24f, cap = StrokeCap.Round)
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
private fun CategorySpendingRow(
    category: TransactionCategory,
    amount: Double,
    percentage: Float,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name.lowercase().capitalize(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "₹${amount.toInt()}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(getCategoryColor(category))
            )
        }
    }
}

private fun getCategoryColor(category: TransactionCategory): Color {
    return when (category) {
        TransactionCategory.FOOD -> Color(0xFFFFB74D)
        TransactionCategory.TRANSPORT -> Color(0xFF64B5F6)
        TransactionCategory.SHOPPING -> Color(0xFFBA68C8)
        TransactionCategory.ENTERTAINMENT -> Color(0xFFF06292)
        TransactionCategory.UTILITIES -> Color(0xFF4DB6AC)
        TransactionCategory.HEALTH -> Color(0xFF81C784)
        TransactionCategory.TRANSFER -> Color(0xFF90A4AE)
        TransactionCategory.OTHER -> Color(0xFFE0E0E0)
    }
}

private fun String.capitalize() = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
