package com.rudradave.kmpfintechstarter.android

import android.app.Application
import com.rudradave.kmpfintechstarter.shared.data.local.LocalFintechSeedDataInitializer
import com.rudradave.kmpfintechstarter.shared.di.androidModule
import com.rudradave.kmpfintechstarter.shared.di.initKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Application entry point that initializes Koin for the Android app. */
class FintechStarterApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** Boots shared and Android-specific dependency graphs. */
    override fun onCreate() {
        super.onCreate()
        val koinApplication = initKoin(platformModules = listOf(androidModule(this)))
        applicationScope.launch {
            koinApplication.koin.get<LocalFintechSeedDataInitializer>().seedIfEmpty()
        }
    }
}
