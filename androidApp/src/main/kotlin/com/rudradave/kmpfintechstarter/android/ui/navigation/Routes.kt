package com.rudradave.kmpfintechstarter.android.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute

@Serializable
data object OnboardingRoute

@Serializable
data object TransactionsRoute

@Serializable
data object ProfileRoute

@Serializable
data class TransactionDetailRoute(val id: String)
