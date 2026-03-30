package com.rudradave.kmpfintechstarter.shared.testutil

import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher

internal class TestDispatcherProvider(
    private val dispatcher: CoroutineDispatcher,
) : DispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}
