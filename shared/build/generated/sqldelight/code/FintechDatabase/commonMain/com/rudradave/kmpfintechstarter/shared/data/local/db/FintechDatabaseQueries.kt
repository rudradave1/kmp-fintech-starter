package com.rudradave.kmpfintechstarter.shared.`data`.local.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Double
import kotlin.Long
import kotlin.String

public class FintechDatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAllTransactions(mapper: (
    id: String,
    merchantName: String,
    amount: Double,
    currency: String,
    category: String,
    status: String,
    timestamp: Long,
    isDebit: Long,
  ) -> T): Query<T> = Query(27_269_565, arrayOf("TransactionEntity"), driver, "FintechDatabase.sq",
      "selectAllTransactions", """
  |SELECT TransactionEntity.id, TransactionEntity.merchantName, TransactionEntity.amount, TransactionEntity.currency, TransactionEntity.category, TransactionEntity.status, TransactionEntity.timestamp, TransactionEntity.isDebit
  |FROM TransactionEntity
  |ORDER BY timestamp DESC
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectAllTransactions(): Query<TransactionEntity> = selectAllTransactions { id,
      merchantName, amount, currency, category, status, timestamp, isDebit ->
    TransactionEntity(
      id,
      merchantName,
      amount,
      currency,
      category,
      status,
      timestamp,
      isDebit
    )
  }

  public fun <T : Any> selectTransactionById(id: String, mapper: (
    id: String,
    merchantName: String,
    amount: Double,
    currency: String,
    category: String,
    status: String,
    timestamp: Long,
    isDebit: Long,
  ) -> T): Query<T> = SelectTransactionByIdQuery(id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectTransactionById(id: String): Query<TransactionEntity> =
      selectTransactionById(id) { id_, merchantName, amount, currency, category, status, timestamp,
      isDebit ->
    TransactionEntity(
      id_,
      merchantName,
      amount,
      currency,
      category,
      status,
      timestamp,
      isDebit
    )
  }

  public fun <T : Any> selectTransactionsByCategory(category: String, mapper: (
    id: String,
    merchantName: String,
    amount: Double,
    currency: String,
    category: String,
    status: String,
    timestamp: Long,
    isDebit: Long,
  ) -> T): Query<T> = SelectTransactionsByCategoryQuery(category) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectTransactionsByCategory(category: String): Query<TransactionEntity> =
      selectTransactionsByCategory(category) { id, merchantName, amount, currency, category_,
      status, timestamp, isDebit ->
    TransactionEntity(
      id,
      merchantName,
      amount,
      currency,
      category_,
      status,
      timestamp,
      isDebit
    )
  }

  public fun <T : Any> selectAccount(mapper: (
    id: String,
    holderName: String,
    balance: Double,
    currency: String,
    maskedCardNumber: String,
    updatedAt: Long,
  ) -> T): Query<T> = Query(247_057_748, arrayOf("AccountEntity"), driver, "FintechDatabase.sq",
      "selectAccount", """
  |SELECT AccountEntity.id, AccountEntity.holderName, AccountEntity.balance, AccountEntity.currency, AccountEntity.maskedCardNumber, AccountEntity.updatedAt
  |FROM AccountEntity
  |LIMIT 1
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getDouble(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectAccount(): Query<AccountEntity> = selectAccount { id, holderName, balance,
      currency, maskedCardNumber, updatedAt ->
    AccountEntity(
      id,
      holderName,
      balance,
      currency,
      maskedCardNumber,
      updatedAt
    )
  }

  public fun upsertTransaction(
    id: String,
    merchantName: String,
    amount: Double,
    currency: String,
    category: String,
    status: String,
    timestamp: Long,
    isDebit: Long,
  ) {
    driver.execute(1_696_118_898, """
        |INSERT OR REPLACE INTO TransactionEntity(
        |    id,
        |    merchantName,
        |    amount,
        |    currency,
        |    category,
        |    status,
        |    timestamp,
        |    isDebit
        |)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 8) {
          bindString(0, id)
          bindString(1, merchantName)
          bindDouble(2, amount)
          bindString(3, currency)
          bindString(4, category)
          bindString(5, status)
          bindLong(6, timestamp)
          bindLong(7, isDebit)
        }
    notifyQueries(1_696_118_898) { emit ->
      emit("TransactionEntity")
    }
  }

  public fun deleteAllTransactions() {
    driver.execute(1_751_479_598, """DELETE FROM TransactionEntity""", 0)
    notifyQueries(1_751_479_598) { emit ->
      emit("TransactionEntity")
    }
  }

  public fun upsertAccount(
    id: String,
    holderName: String,
    balance: Double,
    currency: String,
    maskedCardNumber: String,
    updatedAt: Long,
  ) {
    driver.execute(44_390_849, """
        |INSERT OR REPLACE INTO AccountEntity(
        |    id,
        |    holderName,
        |    balance,
        |    currency,
        |    maskedCardNumber,
        |    updatedAt
        |)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindString(0, id)
          bindString(1, holderName)
          bindDouble(2, balance)
          bindString(3, currency)
          bindString(4, maskedCardNumber)
          bindLong(5, updatedAt)
        }
    notifyQueries(44_390_849) { emit ->
      emit("AccountEntity")
    }
  }

  private inner class SelectTransactionByIdQuery<out T : Any>(
    public val id: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("TransactionEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("TransactionEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-643_813_385, """
    |SELECT TransactionEntity.id, TransactionEntity.merchantName, TransactionEntity.amount, TransactionEntity.currency, TransactionEntity.category, TransactionEntity.status, TransactionEntity.timestamp, TransactionEntity.isDebit
    |FROM TransactionEntity
    |WHERE id = ?
    """.trimMargin(), mapper, 1) {
      bindString(0, id)
    }

    override fun toString(): String = "FintechDatabase.sq:selectTransactionById"
  }

  private inner class SelectTransactionsByCategoryQuery<out T : Any>(
    public val category: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("TransactionEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("TransactionEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-409_301_085, """
    |SELECT TransactionEntity.id, TransactionEntity.merchantName, TransactionEntity.amount, TransactionEntity.currency, TransactionEntity.category, TransactionEntity.status, TransactionEntity.timestamp, TransactionEntity.isDebit
    |FROM TransactionEntity
    |WHERE category = ?
    |ORDER BY timestamp DESC
    """.trimMargin(), mapper, 1) {
      bindString(0, category)
    }

    override fun toString(): String = "FintechDatabase.sq:selectTransactionsByCategory"
  }
}
