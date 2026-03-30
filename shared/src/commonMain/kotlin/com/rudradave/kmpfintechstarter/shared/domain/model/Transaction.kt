package com.rudradave.kmpfintechstarter.shared.domain.model

/** Represents a single account transaction in the fintech domain. */
data class Transaction(
    val id: String,
    val merchantName: String,
    val amount: Double,
    val currency: String,
    val category: TransactionCategory,
    val status: TransactionStatus,
    val timestamp: Long,
    val isDebit: Boolean,
)

/** Supported transaction categories shown across the experience. */
enum class TransactionCategory {
    FOOD,
    TRANSPORT,
    SHOPPING,
    ENTERTAINMENT,
    UTILITIES,
    HEALTH,
    TRANSFER,
    OTHER,
}

/** Processing state of a transaction. */
enum class TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED,
}
