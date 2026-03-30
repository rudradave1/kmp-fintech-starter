package com.rudradave.kmpfintechstarter.shared.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.rudradave.kmpfintechstarter.shared.data.local.db.FintechDatabase
import com.rudradave.kmpfintechstarter.shared.platform.IosSyncMetadataStore
import com.rudradave.kmpfintechstarter.shared.platform.SyncMetadataStore
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.KoinApplication
import org.koin.dsl.module

/** iOS-specific Koin module for native engine and database bindings. */
val iosModule = module {
    single<SqlDriver> { NativeSqliteDriver(FintechDatabase.Schema, "FintechDatabase.db") }
    single<HttpClientEngineFactory<*>> { Darwin }
    single<SyncMetadataStore> { IosSyncMetadataStore() }
}

/** Starts shared Koin for SwiftUI entry points. */
fun initKoinIos(): KoinApplication = initKoin(platformModules = listOf(iosModule))
