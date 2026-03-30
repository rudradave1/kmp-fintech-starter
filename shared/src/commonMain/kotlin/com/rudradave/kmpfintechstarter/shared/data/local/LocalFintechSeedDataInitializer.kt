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
        val existingAccount = queries.selectAccount().executeAsOneOrNull()
        val hasAccount = existingAccount != null

        if (hasTransactions && hasAccount && !shouldRefreshDemoAccount(existingAccount)) {
            return
        }

        val now = currentTimeMillis()
        queries.transaction {
            if (!hasAccount || shouldRefreshDemoAccount(existingAccount)) {
                queries.upsertAccount(
                    id = "account-demo-primary",
                    holderName = "Neha Iyer",
                    balance = 124892.50,
                    currency = "INR",
                    maskedCardNumber = "\u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 1184",
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

    private fun shouldRefreshDemoAccount(account: com.rudradave.kmpfintechstarter.shared.data.local.db.AccountEntity?): Boolean {
        if (account == null) return false
        return account.id == "account-demo-primary" ||
            account.holderName == "Rudra Dave" ||
            account.holderName == "Aryan Mehta" ||
            account.holderName == "Neha Iyer"
    }

    private fun demoTransactions(now: Long): List<SeedTransaction> {
        val day = 24L * 60L * 60L * 1000L
        return listOf(
            SeedTransaction("txn-demo-001", "Swiggy", 347.00, "INR", "FOOD", "COMPLETED", now - (2 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-002", "Uber", 234.00, "INR", "TRANSPORT", "COMPLETED", now - (4 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-003", "Spotify", 119.00, "INR", "ENTERTAINMENT", "COMPLETED", now - (6 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-004", "Salary Credit", 73000.00, "INR", "TRANSFER", "COMPLETED", now - (9 * 60L * 60L * 1000L), 0L),
            SeedTransaction("txn-demo-005", "Airtel Recharge", 299.00, "INR", "UTILITIES", "COMPLETED", now - (11 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-006", "Zepto", 892.50, "INR", "FOOD", "COMPLETED", now - day - (2 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-007", "Ola", 189.00, "INR", "TRANSPORT", "COMPLETED", now - day - (4 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-008", "Netflix", 649.00, "INR", "ENTERTAINMENT", "COMPLETED", now - day - (6 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-009", "Amazon", 1299.00, "INR", "SHOPPING", "COMPLETED", now - day - (8 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-010", "Apollo Pharmacy", 567.00, "INR", "HEALTH", "COMPLETED", now - day - (10 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-011", "PhonePe Transfer", 5000.00, "INR", "TRANSFER", "COMPLETED", now - (2 * day) - (3 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-012", "Myntra", 2499.00, "INR", "SHOPPING", "COMPLETED", now - (2 * day) - (7 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-013", "Zomato", 445.00, "INR", "FOOD", "COMPLETED", now - (3 * day) - (1 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-014", "Blinkit", 634.00, "INR", "FOOD", "COMPLETED", now - (3 * day) - (5 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-015", "BookMyShow", 798.00, "INR", "ENTERTAINMENT", "COMPLETED", now - (4 * day) - (2 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-016", "Rapido", 89.00, "INR", "TRANSPORT", "COMPLETED", now - (5 * day) - (4 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-017", "Freelance Credit", 15000.00, "INR", "TRANSFER", "COMPLETED", now - (6 * day) - (3 * 60L * 60L * 1000L), 0L),
            SeedTransaction("txn-demo-018", "BESCOM Electricity", 1847.00, "INR", "UTILITIES", "COMPLETED", now - (31 * day) - (2 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-019", "HDFC Loan EMI", 8500.00, "INR", "UTILITIES", "PENDING", now - (33 * day) - (6 * 60L * 60L * 1000L), 1L),
            SeedTransaction("txn-demo-020", "Refund - Myntra", 2499.00, "INR", "SHOPPING", "REFUNDED", now - (36 * day) - (4 * 60L * 60L * 1000L), 0L),
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
