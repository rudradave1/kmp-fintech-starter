package com.rudradave.kmpfintechstarter.shared.domain.usecase

import com.rudradave.kmpfintechstarter.shared.domain.model.Account
import com.rudradave.kmpfintechstarter.shared.domain.model.DashboardState
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.repository.AccountRepository
import com.rudradave.kmpfintechstarter.shared.domain.repository.TransactionRepository
import com.rudradave.kmpfintechstarter.shared.platform.currentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/** Combines account and transaction streams into a dashboard-ready state. */
class GetDashboardDataUseCase(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
) {
    /** Invokes the use case. */
    operator fun invoke(): Flow<DashboardState> {
        return combine(
            accountRepository.getAccount(),
            transactionRepository.getTransactions(),
        ) { account, transactions ->
            val now = currentTimeMillis()
            val thirtyDaysInMillis = 30L * 24L * 60L * 60L * 1000L
            val monthlyTransactions = transactions.filter { now - it.timestamp <= thirtyDaysInMillis }
            DashboardState(
                account = account ?: Account(
                    id = "",
                    holderName = "",
                    balance = 0.0,
                    currency = "",
                    maskedCardNumber = "",
                ),
                recentTransactions = transactions.take(5),
                monthlySpend = monthlyTransactions.filter(Transaction::isDebit).sumOf(Transaction::amount),
                monthlyIncome = monthlyTransactions.filterNot(Transaction::isDebit).sumOf(Transaction::amount),
            )
        }
    }
}
