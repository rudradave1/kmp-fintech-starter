package com.rudradave.kmpfintechstarter.shared.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.rudradave.kmpfintechstarter.shared.data.local.db.FintechDatabase
import com.rudradave.kmpfintechstarter.shared.data.repository.UserPreferencesRepositoryImpl
import com.rudradave.kmpfintechstarter.shared.domain.repository.UserPreferencesRepository
import com.rudradave.kmpfintechstarter.shared.platform.IosSecurityManager
import com.rudradave.kmpfintechstarter.shared.platform.IosSyncMetadataStore
import com.rudradave.kmpfintechstarter.shared.platform.SecurityManager
import com.rudradave.kmpfintechstarter.shared.platform.SyncMetadataStore
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.KoinApplication
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import okio.Path.Companion.toPath

/** iOS-specific Koin module for native engine and database bindings. */
@OptIn(ExperimentalForeignApi::class)
val iosModule = module {
    single<SqlDriver> { NativeSqliteDriver(FintechDatabase.Schema, "FintechDatabase.db") }
    single<HttpClientEngineFactory<*>> { Darwin }
    single<SyncMetadataStore> { IosSyncMetadataStore() }
    single<SecurityManager> { IosSecurityManager() }
    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl {
            val documentDirectory: NSURL = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )!!
            (documentDirectory.path!! + "/user_prefs.preferences_pb").toPath()
        }
    }
}

/** Starts shared Koin for SwiftUI entry points. */
fun initKoinIos(): KoinApplication = initKoin(platformModules = listOf(iosModule))
