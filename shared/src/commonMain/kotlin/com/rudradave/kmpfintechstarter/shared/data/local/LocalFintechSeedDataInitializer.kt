package com.rudradave.kmpfintechstarter.shared.data.local

import com.rudradave.kmpfintechstarter.shared.data.local.db.FintechDatabase
import com.rudradave.kmpfintechstarter.shared.platform.currentTimeMillis

/** Seeds local account and transaction data so the starter is testable before backend wiring exists. */
class LocalFintechSeedDataInitializer(
    private val database: FintechDatabase,
) {
    /** Inserts demo account and transaction history only when the local database is empty. */
    fun seedIfEmpty() {
        val queries = database.fintechDatabaseQueries
        val hasTransactions = queries.selectAllTransactions().executeAsList().isNotEmpty()
        val hasAccount = queries.selectAccount().executeAsOneOrNull() != null

        if (hasTransactions && hasAccount) {
            return
        }

        val now = currentTimeMillis()
        queries.transaction {
            if (!hasAccount) {
                queries.upsertAccount(
                    id = "account-demo-primary",
                    holderName = "Rudra Dave",
                    balance = 4825.76,
                    currency = "USD",
                    maskedCardNumber = "**** 4821",
                    updatedAt = now,
                )
            }

            if (!hasTransactions) {
                demoTransactions(now).forEach { transaction ->
                    queries.upsertTransaction(
                        id = transaction.id,
                        merchantName = transaction.merchantName,
                        amount = transaction.amount,
                        currency = transaction.currency,
                        category = transaction.category,
                        status = transaction.status,
                        timestamp = transaction.timestamp,
                        isDebit = transaction.isDebit,
                    )
                }
            }
        }
    }

    private fun demoTransactions(now: Long): List<SeedTransaction> {
        val day = 24L * 60L * 60L * 1000L
        return listOf(
            SeedTransaction("txn-demo-001", "Blue Bottle Coffee", 8.45, "USD", "FOOD", "COMPLETED", now - day, 1L),
            SeedTransaction("txn-demo-002", "Uber", 16.20, "USD", "TRANSPORT", "COMPLETED", now - day, 1L),
            SeedTransaction("txn-demo-003", "Payroll Deposit", 2450.00, "USD", "TRANSFER", "COMPLETED", now - (2 * day), 0L),
            SeedTransaction("txn-demo-004", "Netflix", 14.99, "USD", "ENTERTAINMENT", "COMPLETED", now - (3 * day), 1L),
            SeedTransaction("txn-demo-005", "CVS Pharmacy", 24.15, "USD", "HEALTH", "COMPLETED", now - (4 * day), 1L),
            SeedTransaction("txn-demo-006", "Whole Foods", 92.38, "USD", "FOOD", "PENDING", now - (5 * day), 1L),
            SeedTransaction("txn-demo-007", "Amazon", 128.74, "USD", "SHOPPING", "REFUNDED", now - (6 * day), 1L),
            SeedTransaction("txn-demo-008", "Electric Utility", 78.10, "USD", "UTILITIES", "COMPLETED", now - (8 * day), 1L),
            SeedTransaction("txn-demo-009", "Friend Transfer", 300.00, "USD", "TRANSFER", "FAILED", now - (10 * day), 1L),
            SeedTransaction("txn-demo-010", "Apple Store", 54.99, "USD", "SHOPPING", "COMPLETED", now - (12 * day), 1L),
        )
    }
}

private data class SeedTransaction(
    val id: String,
    val merchantName: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val status: String,
    val timestamp: Long,
    val isDebit: Long,
)
