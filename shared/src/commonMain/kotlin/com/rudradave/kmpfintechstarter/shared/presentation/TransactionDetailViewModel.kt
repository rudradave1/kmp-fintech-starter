package com.rudradave.kmpfintechstarter.shared.presentation

import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.usecase.GetTransactionByIdUseCase
import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/** Shared state holder for the transaction detail experience. */
class TransactionDetailViewModel internal constructor(
    private val transactionId: String,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : CoroutineScope {
    private val job: Job = SupervisorJob()
    override val coroutineContext = job + dispatcherProvider.main

    private val _uiState = MutableStateFlow(TransactionDetailUiState(isLoading = true))
    val uiState: StateFlow<TransactionDetailUiState> = _uiState.asStateFlow()

    init {
        launch(dispatcherProvider.io) {
            getTransactionByIdUseCase(transactionId).collectLatest { transaction ->
                _uiState.value = _uiState.value.copy(
                    transaction = transaction,
                    isLoading = false,
                    error = if (transaction == null) "Transaction unavailable" else null,
                )
            }
        }
    }

    /** Releases resources owned by the state holder. */
    fun clear() {
        cancel()
    }
}

/** Immutable UI state exposed by [TransactionDetailViewModel]. */
data class TransactionDetailUiState(
    val transaction: Transaction? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
