package com.rudradave.kmpfintechstarter.shared.domain.model

/** Account summary rendered on the dashboard. */
data class Account(
    val id: String,
    val holderName: String,
    val balance: Double,
    val currency: String,
    val maskedCardNumber: String,
)
