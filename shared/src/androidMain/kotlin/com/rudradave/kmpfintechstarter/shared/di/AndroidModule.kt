package com.rudradave.kmpfintechstarter.shared.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.db.SqlDriver
import com.rudradave.kmpfintechstarter.shared.data.local.db.FintechDatabase
import androidx.datastore.preferences.preferencesDataStoreFile
import com.rudradave.kmpfintechstarter.shared.data.repository.UserPreferencesRepositoryImpl
import com.rudradave.kmpfintechstarter.shared.domain.repository.UserPreferencesRepository
import com.rudradave.kmpfintechstarter.shared.platform.AndroidSecurityManager
import com.rudradave.kmpfintechstarter.shared.platform.AndroidSyncMetadataStore
import com.rudradave.kmpfintechstarter.shared.platform.SecurityManager
import com.rudradave.kmpfintechstarter.shared.platform.SyncMetadataStore
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module
import okio.Path.Companion.toOkioPath

/** Android-only Koin module that binds context-backed dependencies. */
fun androidModule(context: Context) = module {
    single<Context> { context.applicationContext }
    single<SqlDriver> { AndroidSqliteDriver(FintechDatabase.Schema, get(), "FintechDatabase.db") }
    single<HttpClientEngineFactory<*>> { OkHttp }
    single<SyncMetadataStore> { AndroidSyncMetadataStore(get()) }
    single<SecurityManager> { AndroidSecurityManager(get(), get()) }
    single<UserPreferencesRepository> { 
        UserPreferencesRepositoryImpl { 
            context.preferencesDataStoreFile("user_prefs.preferences_pb").toOkioPath() 
        } 
    }
}
