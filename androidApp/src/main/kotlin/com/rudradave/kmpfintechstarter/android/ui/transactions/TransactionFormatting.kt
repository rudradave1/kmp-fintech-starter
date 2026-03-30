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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.theme.CategoryEntertainment
import com.rudradave.kmpfintechstarter.android.ui.theme.CategoryFood
import com.rudradave.kmpfintechstarter.android.ui.theme.CategoryHealth
import com.rudradave.kmpfintechstarter.android.ui.theme.CategoryShopping
import com.rudradave.kmpfintechstarter.android.ui.theme.CategoryTransfer
import com.rudradave.kmpfintechstarter.android.ui.theme.CategoryTransport
import com.rudradave.kmpfintechstarter.android.ui.theme.CategoryUtilities
import com.rudradave.kmpfintechstarter.android.ui.theme.StatusCompleted
import com.rudradave.kmpfintechstarter.android.ui.theme.StatusFailed
import com.rudradave.kmpfintechstarter.android.ui.theme.StatusPending
import com.rudradave.kmpfintechstarter.android.ui.theme.StatusRefunded
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale
import java.util.UUID

private val indianLocale = Locale("en", "IN")
private val headerMonthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", indianLocale)
private val detailDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", indianLocale)

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

internal fun TransactionCategory.emoji(): String {
    return when (this) {
        TransactionCategory.FOOD -> "🍔"
        TransactionCategory.TRANSPORT -> "🚗"
        TransactionCategory.ENTERTAINMENT -> "🎬"
        TransactionCategory.SHOPPING -> "🛍️"
        TransactionCategory.HEALTH -> "💊"
        TransactionCategory.UTILITIES -> "⚡"
        TransactionCategory.TRANSFER -> "💸"
        TransactionCategory.OTHER -> "💳"
    }
}

internal fun TransactionCategory.tint(): Color {
    return when (this) {
        TransactionCategory.FOOD -> CategoryFood
        TransactionCategory.TRANSPORT -> CategoryTransport
        TransactionCategory.SHOPPING -> CategoryShopping
        TransactionCategory.ENTERTAINMENT -> CategoryEntertainment
        TransactionCategory.UTILITIES -> CategoryUtilities
        TransactionCategory.HEALTH -> CategoryHealth
        TransactionCategory.TRANSFER -> CategoryTransfer
        TransactionCategory.OTHER -> CategoryTransfer
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

internal fun TransactionStatus.tint(): Color {
    return when (this) {
        TransactionStatus.PENDING -> StatusPending
        TransactionStatus.COMPLETED -> StatusCompleted
        TransactionStatus.FAILED -> StatusFailed
        TransactionStatus.REFUNDED -> StatusRefunded
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
    val prefix = if (isDebit) "-" else "+"
    return prefix + formatCurrencyAmount(amount = amount, currencyCode = currency)
}

internal fun formatCurrencyAmount(amount: Double, currencyCode: String): String {
    return NumberFormat.getCurrencyInstance(indianLocale).apply {
        currency = resolvedCurrency(currencyCode)
    }.format(amount)
}

internal fun Transaction.groupDate(): LocalDate {
    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
}

internal fun Long.formatDateTime(): String {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(detailDateFormatter)
}

@StringRes
internal fun LocalDate.relativeHeaderRes(now: LocalDate = LocalDate.now()): Int? {
    return when {
        this == now -> R.string.group_today
        this == now.minusDays(1) -> R.string.group_yesterday
        year == now.year && month == now.month && isAfter(now.minusDays(7)) -> R.string.group_this_week
        year == now.minusMonths(1).year && month == now.minusMonths(1).month -> R.string.group_last_month
        else -> null
    }
}

internal fun LocalDate.formatMonthHeader(): String {
    return format(headerMonthFormatter)
}

internal fun Transaction.referenceId(): String {
    return UUID.nameUUIDFromBytes(id.toByteArray()).toString()
}

internal fun Transaction.typeLabelRes(): Int {
    return if (isDebit) R.string.debit_label else R.string.credit_label
}

internal fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

private fun resolvedCurrency(currencyCode: String): Currency {
    return runCatching { Currency.getInstance(currencyCode) }
        .getOrElse { Currency.getInstance(indianLocale) }
}
