package com.rudradave.kmpfintechstarter.shared.platform

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Provides dispatchers so shared logic stays platform agnostic and testable. */
interface DispatcherProvider {
    /** Dispatcher used for UI-oriented state updates. */
    val main: CoroutineDispatcher

    /** Dispatcher used for IO and persistence work. */
    val io: CoroutineDispatcher

    /** Dispatcher used for CPU-bound transformations. */
    val default: CoroutineDispatcher
}

internal class DefaultDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Default
    override val io: CoroutineDispatcher = Dispatchers.Default
    override val default: CoroutineDispatcher = Dispatchers.Default
}
