package com.rudradave.kmpfintechstarter.shared.util

import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import kotlin.math.absoluteValue

/** Shared utility to format currency consistently across platforms. */
fun formatCurrency(amount: Double, currencyCode: String): String {
    val symbol = when (currencyCode) {
        "INR" -> "₹"
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        else -> currencyCode
    }
    
    // Simple formatting for template demonstration
    val formattedAmount = amount.absoluteValue.toString().let {
        if (it.contains(".")) {
            val parts = it.split(".")
            val whole = parts[0]
            val decimal = parts[1].take(2).padEnd(2, '0')
            "$whole.$decimal"
        } else {
            "$it.00"
        }
    }
    
    return if (amount < 0) "-$symbol$formattedAmount" else "$symbol$formattedAmount"
}

fun Transaction.formattedAmountShared(): String {
    val base = formatCurrency(amount, currency)
    return if (isDebit) "-$base" else "+$base"
}
