package com.rudradave.kmpfintechstarter.android.ui.transactions

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Currency
import java.util.Locale

internal fun TransactionCategory.icon(): ImageVector {
    return when (this) {
        TransactionCategory.FOOD -> Icons.Outlined.LocalDining
        TransactionCategory.TRANSPORT -> Icons.Outlined.Train
        TransactionCategory.SHOPPING -> Icons.Outlined.LocalMall
        TransactionCategory.ENTERTAINMENT -> Icons.Outlined.LocalActivity
        TransactionCategory.UTILITIES -> Icons.Outlined.WaterDrop
        TransactionCategory.HEALTH -> Icons.Outlined.LocalHospital
        TransactionCategory.TRANSFER -> Icons.Outlined.SwapHoriz
        TransactionCategory.OTHER -> Icons.Outlined.Category
    }
}

@StringRes
internal fun TransactionCategory.labelRes(): Int {
    return when (this) {
        TransactionCategory.FOOD -> R.string.category_food
        TransactionCategory.TRANSPORT -> R.string.category_transport
        TransactionCategory.SHOPPING -> R.string.category_shopping
        TransactionCategory.ENTERTAINMENT -> R.string.category_entertainment
        TransactionCategory.UTILITIES -> R.string.category_utilities
        TransactionCategory.HEALTH -> R.string.category_health
        TransactionCategory.TRANSFER -> R.string.category_transfer
        TransactionCategory.OTHER -> R.string.category_other
    }
}

@StringRes
internal fun TransactionStatus.labelRes(): Int {
    return when (this) {
        TransactionStatus.PENDING -> R.string.status_pending
        TransactionStatus.COMPLETED -> R.string.status_completed
        TransactionStatus.FAILED -> R.string.status_failed
        TransactionStatus.REFUNDED -> R.string.status_refunded
    }
}

internal fun TransactionStatus.icon(): ImageVector {
    return when (this) {
        TransactionStatus.PENDING -> Icons.Outlined.Pending
        TransactionStatus.COMPLETED -> Icons.Outlined.CheckCircle
        TransactionStatus.FAILED -> Icons.Outlined.Warning
        TransactionStatus.REFUNDED -> Icons.Outlined.Favorite
    }
}

internal fun Transaction.formattedAmount(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        currency = resolvedCurrency(this@formattedAmount.currency)
    }
    val prefix = if (isDebit) "-" else "+"
    return prefix + formatter.format(amount)
}

internal fun formatCurrencyAmount(amount: Double, currencyCode: String): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        currency = resolvedCurrency(currencyCode)
    }.format(amount)
}

internal fun Transaction.groupDate(): LocalDate {
    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
}

internal fun Long.formatDateTime(): String {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
}

internal fun LocalDate.formatForHeader(): String {
    return format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
}

private fun resolvedCurrency(currencyCode: String): Currency {
    return runCatching { Currency.getInstance(currencyCode) }
        .getOrElse { Currency.getInstance(Locale.getDefault()) }
}
