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
    single<SqlDriver> { 
        val context = get<Context>()
        net.sqlcipher.database.SQLiteDatabase.loadLibs(context)
        
        val secureStorage = get<com.rudradave.kmpfintechstarter.shared.data.local.SecureStorage>()

        val passphrase = secureStorage.getDatabasePassphrase()
        
        // Safety check: If a database exists but is unencrypted or encrypted with a different key, 
        // opening it with SQLCipher will fail. For development/starter, we delete the 
        // incompatible file so it can be recreated with the current encryption key.
        val dbName = "FintechDatabase.db"
        val dbFile = context.getDatabasePath(dbName)
        if (dbFile.exists()) {
            var isDatabaseValid = false
            var db: net.sqlcipher.database.SQLiteDatabase? = null
            try {
                db = net.sqlcipher.database.SQLiteDatabase.openDatabase(
                    dbFile.absolutePath, 
                    passphrase, 
                    null, 
                    net.sqlcipher.database.SQLiteDatabase.OPEN_READONLY
                )
                val cursor = db.rawQuery("SELECT count(*) FROM sqlite_master", null)
                isDatabaseValid = cursor.moveToFirst()
                cursor.close()
            } catch (e: Exception) {
                isDatabaseValid = false
            } finally {
                db?.close()
            }

            if (!isDatabaseValid) {
                context.deleteDatabase(dbName)
            }
        }

        val hook = object : net.sqlcipher.database.SQLiteDatabaseHook {
            override fun preKey(database: net.sqlcipher.database.SQLiteDatabase?) {}
            override fun postKey(database: net.sqlcipher.database.SQLiteDatabase?) {
                database?.rawQuery("PRAGMA page_size = 16384", null)?.close()
                database?.rawQuery("PRAGMA journal_mode = WAL", null)?.close()
            }
        }

        val factory = net.sqlcipher.database.SupportFactory(passphrase.toByteArray(), hook)



        
        AndroidSqliteDriver(
            schema = FintechDatabase.Schema, 
            context = get(), 
            name = "FintechDatabase.db",
            factory = factory
        ) 
    }

    single<HttpClientEngineFactory<*>> { OkHttp }
    single<SyncMetadataStore> { AndroidSyncMetadataStore(get()) }
    single<SecurityManager> { AndroidSecurityManager(get(), get()) }
    single<com.rudradave.kmpfintechstarter.shared.data.local.SecureStorage> {
        val prefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
        com.rudradave.kmpfintechstarter.shared.data.local.SecureStorageImpl(
            com.russhwolf.settings.SharedPreferencesSettings(prefs)
        )

    }
    single<UserPreferencesRepository> { 
        UserPreferencesRepositoryImpl { 
            context.preferencesDataStoreFile("user_prefs.preferences_pb").toOkioPath() 
        } 
    }
}

