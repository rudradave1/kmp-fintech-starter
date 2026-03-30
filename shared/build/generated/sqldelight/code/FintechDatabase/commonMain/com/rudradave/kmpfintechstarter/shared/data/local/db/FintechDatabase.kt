package com.rudradave.kmpfintechstarter.shared.`data`.local.db

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.rudradave.kmpfintechstarter.shared.`data`.local.db.shared.newInstance
import com.rudradave.kmpfintechstarter.shared.`data`.local.db.shared.schema
import kotlin.Unit

public interface FintechDatabase : Transacter {
  public val fintechDatabaseQueries: FintechDatabaseQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = FintechDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): FintechDatabase =
        FintechDatabase::class.newInstance(driver)
  }
}
