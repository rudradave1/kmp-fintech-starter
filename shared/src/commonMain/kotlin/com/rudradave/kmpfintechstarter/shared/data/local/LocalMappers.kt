package com.rudradave.kmpfintechstarter.shared.data.local

import com.rudradave.kmpfintechstarter.shared.data.local.db.AccountEntity
import com.rudradave.kmpfintechstarter.shared.data.local.db.TransactionEntity
import com.rudradave.kmpfintechstarter.shared.data.remote.AccountDto
import com.rudradave.kmpfintechstarter.shared.data.remote.TransactionDto
import com.rudradave.kmpfintechstarter.shared.domain.model.Account
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus

internal fun TransactionEntity.toDomainTransaction(): Transaction {
    return Transaction(
        id = id,
        merchantName = merchantName,
        amount = amount,
        currency = currency,
        category = category.toTransactionCategory(),
        status = status.toTransactionStatus(),
        timestamp = timestamp,
        isDebit = isDebit == 1L,
    )
}

internal fun AccountEntity.toDomainAccount(): Account {
    return Account(
        id = id,
        holderName = holderName,
        balance = balance,
        currency = currency,
        maskedCardNumber = maskedCardNumber,
    )
}

internal fun TransactionDto.normalizedDto(): TransactionDto {
    return copy(
        category = category.uppercase(),
        status = status.uppercase(),
    )
}

internal fun AccountDto.toDomainAccount(): Account {
    return Account(
        id = id,
        holderName = holderName,
        balance = balance,
        currency = currency,
        maskedCardNumber = maskedCardNumber,
    )
}

private fun String.toTransactionCategory(): TransactionCategory {
    return TransactionCategory.entries.firstOrNull { entry -> entry.name == uppercase() }
        ?: TransactionCategory.OTHER
}

private fun String.toTransactionStatus(): TransactionStatus {
    return TransactionStatus.entries.firstOrNull { entry -> entry.name == uppercase() }
        ?: TransactionStatus.FAILED
}
