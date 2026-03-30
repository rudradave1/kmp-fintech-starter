package com.rudradave.kmpfintechstarter.shared.testutil

import com.rudradave.kmpfintechstarter.shared.domain.model.Account
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus

internal object SampleData {
    fun account(balance: Double = 1_850.50): Account {
        return Account(
            id = "acc-1",
            holderName = "Aarav Kapoor",
            balance = balance,
            currency = "INR",
            maskedCardNumber = "**** 1184",
        )
    }

    fun transactions(): List<Transaction> {
        return listOf(
            transaction(
                id = "txn-1",
                category = TransactionCategory.FOOD,
                amount = 347.00,
                status = TransactionStatus.COMPLETED,
                timestamp = 1_717_286_400_000,
                isDebit = true,
            ),
            transaction(
                id = "txn-2",
                category = TransactionCategory.TRANSFER,
                amount = 5_000.00,
                status = TransactionStatus.COMPLETED,
                timestamp = 1_717_372_800_000,
                isDebit = false,
            ),
            transaction(
                id = "txn-3",
                category = TransactionCategory.TRANSPORT,
                amount = 234.00,
                status = TransactionStatus.PENDING,
                timestamp = 1_717_459_200_000,
                isDebit = true,
            ),
        )
    }

    fun transaction(
        id: String = "txn-default",
        category: TransactionCategory = TransactionCategory.OTHER,
        amount: Double = 42.0,
        status: TransactionStatus = TransactionStatus.COMPLETED,
        timestamp: Long = 1_717_545_600_000,
        isDebit: Boolean = true,
    ): Transaction {
        return Transaction(
            id = id,
            merchantName = "Merchant $id",
            amount = amount,
            currency = "INR",
            category = category,
            status = status,
            timestamp = timestamp,
            isDebit = isDebit,
        )
    }
}
