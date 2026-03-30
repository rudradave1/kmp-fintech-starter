package com.rudradave.kmpfintechstarter.shared.domain.model

/** Aggregated dashboard data built from account and transaction sources. */
data class DashboardState(
    val account: Account,
    val recentTransactions: List<Transaction>,
    val monthlySpend: Double,
    val monthlyIncome: Double,
)
