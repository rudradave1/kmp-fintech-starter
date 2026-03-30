package com.rudradave.kmpfintechstarter.shared.`data`.local.db

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class TransactionEntity(
  public val id: String,
  public val merchantName: String,
  public val amount: Double,
  public val currency: String,
  public val category: String,
  public val status: String,
  public val timestamp: Long,
  public val isDebit: Long,
)
