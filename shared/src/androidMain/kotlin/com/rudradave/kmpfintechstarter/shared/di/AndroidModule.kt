package com.rudradave.kmpfintechstarter.shared.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.db.SqlDriver
import com.rudradave.kmpfintechstarter.shared.data.local.db.FintechDatabase
import com.rudradave.kmpfintechstarter.shared.platform.AndroidSyncMetadataStore
import com.rudradave.kmpfintechstarter.shared.platform.SyncMetadataStore
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

/** Android-only Koin module that binds context-backed dependencies. */
fun androidModule(context: Context) = module {
    single<Context> { context.applicationContext }
    single<SqlDriver> { AndroidSqliteDriver(FintechDatabase.Schema, get(), "FintechDatabase.db") }
    single<HttpClientEngineFactory<*>> { OkHttp }
    single<SyncMetadataStore> { AndroidSyncMetadataStore(get()) }
}
