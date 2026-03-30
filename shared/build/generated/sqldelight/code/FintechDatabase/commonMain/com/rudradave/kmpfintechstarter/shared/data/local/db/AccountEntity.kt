package com.rudradave.kmpfintechstarter.shared.`data`.local.db

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class AccountEntity(
  public val id: String,
  public val holderName: String,
  public val balance: Double,
  public val currency: String,
  public val maskedCardNumber: String,
  public val updatedAt: Long,
)
