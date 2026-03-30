package com.rudradave.kmpfintechstarter.shared.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TransactionDto(
    val id: String,
    @SerialName("merchant_name") val merchantName: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val status: String,
    val timestamp: Long,
    @SerialName("is_debit") val isDebit: Boolean,
)

@Serializable
internal data class AccountDto(
    val id: String,
    @SerialName("holder_name") val holderName: String,
    val balance: Double,
    val currency: String,
    @SerialName("masked_card_number") val maskedCardNumber: String,
)
