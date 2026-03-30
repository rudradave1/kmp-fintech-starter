package com.rudradave.kmpfintechstarter.shared.di

import app.cash.sqldelight.db.SqlDriver
import com.rudradave.kmpfintechstarter.shared.data.local.LocalFintechSeedDataInitializer
import com.rudradave.kmpfintechstarter.shared.data.local.db.FintechDatabase
import com.rudradave.kmpfintechstarter.shared.data.remote.FintechApiService
import com.rudradave.kmpfintechstarter.shared.data.remote.NetworkErrorMapper
import com.rudradave.kmpfintechstarter.shared.data.remote.createHttpClient
import com.rudradave.kmpfintechstarter.shared.data.repository.AccountRepositoryImpl
import com.rudradave.kmpfintechstarter.shared.data.repository.TransactionRepositoryImpl
import com.rudradave.kmpfintechstarter.shared.domain.repository.AccountRepository
import com.rudradave.kmpfintechstarter.shared.domain.repository.TransactionRepository
import com.rudradave.kmpfintechstarter.shared.domain.usecase.FilterTransactionsByCategoryUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.GetDashboardDataUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.GetTransactionByIdUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.GetTransactionsUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.SyncDashboardDataUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.SyncTransactionsUseCase
import com.rudradave.kmpfintechstarter.shared.platform.DefaultDispatcherProvider
import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import com.rudradave.kmpfintechstarter.shared.presentation.DashboardViewModel
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionDetailViewModel
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionViewModel
import io.ktor.client.engine.HttpClientEngineFactory
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/** Shared Koin module that provides the networking stack. */
val networkModule: Module = module {
    single { NetworkErrorMapper() }
    single { createHttpClient(get<HttpClientEngineFactory<*>>()) }
    single { FintechApiService(get(), get()) }
}

/** Shared Koin module that provides the SQLDelight database. */
val databaseModule: Module = module {
    single { FintechDatabase(get<SqlDriver>()) }
    single { LocalFintechSeedDataInitializer(get()) }
}

/** Shared Koin module that provides repository implementations. */
val repositoryModule: Module = module {
    single<TransactionRepository> { TransactionRepositoryImpl(get(), get(), get()) }
    single<AccountRepository> { AccountRepositoryImpl(get(), get(), get()) }
}

/** Shared Koin module that provides all use cases. */
val useCaseModule: Module = module {
    factory { GetTransactionsUseCase(get()) }
    factory { GetDashboardDataUseCase(get(), get()) }
    factory { SyncTransactionsUseCase(get(), get()) }
    factory { FilterTransactionsByCategoryUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
    factory { SyncDashboardDataUseCase(get(), get(), get()) }
}

/** Shared Koin module that provides platform-agnostic state holders. */
val viewModelModule: Module = module {
    factory { TransactionViewModel(get(), get(), get(), get()) }
    factory { DashboardViewModel(get(), get(), get()) }
    factory { (transactionId: String) -> TransactionDetailViewModel(transactionId, get(), get()) }
}

/** Shared Koin module that provides coroutine dispatchers. */
val sharedRuntimeModule: Module = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}

/** Returns the shared modules that every platform should load. */
fun sharedModules(): List<Module> {
    return listOf(
        sharedRuntimeModule,
        networkModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
    )
}

/** Starts Koin with shared modules plus any platform-specific bindings. */
fun initKoin(platformModules: List<Module>, appDeclaration: KoinApplication.() -> Unit = {}): KoinApplication {
    return startKoin {
        appDeclaration()
        modules(sharedModules() + platformModules)
    }
}
