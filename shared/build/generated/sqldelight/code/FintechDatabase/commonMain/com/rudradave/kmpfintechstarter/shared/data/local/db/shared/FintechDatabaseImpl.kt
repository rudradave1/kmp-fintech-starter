package com.rudradave.kmpfintechstarter.shared.`data`.local.db.shared

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.rudradave.kmpfintechstarter.shared.`data`.local.db.FintechDatabase
import com.rudradave.kmpfintechstarter.shared.`data`.local.db.FintechDatabaseQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<FintechDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = FintechDatabaseImpl.Schema

internal fun KClass<FintechDatabase>.newInstance(driver: SqlDriver): FintechDatabase =
    FintechDatabaseImpl(driver)

private class FintechDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), FintechDatabase {
  override val fintechDatabaseQueries: FintechDatabaseQueries = FintechDatabaseQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE TransactionEntity (
          |    id TEXT NOT NULL PRIMARY KEY,
          |    merchantName TEXT NOT NULL,
          |    amount REAL NOT NULL,
          |    currency TEXT NOT NULL,
          |    category TEXT NOT NULL,
          |    status TEXT NOT NULL,
          |    timestamp INTEGER NOT NULL,
          |    isDebit INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE AccountEntity (
          |    id TEXT NOT NULL PRIMARY KEY,
          |    holderName TEXT NOT NULL,
          |    balance REAL NOT NULL,
          |    currency TEXT NOT NULL,
          |    maskedCardNumber TEXT NOT NULL,
          |    updatedAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
