package com.rudradave.kmpfintechstarter.shared.presentation

import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.usecase.FilterTransactionsByCategoryUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.GetTransactionsUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.SyncTransactionsUseCase
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

/** Shared state holder for the transaction list experience. */
class TransactionViewModel(
    private val getTransactions: GetTransactionsUseCase,
    private val syncTransactions: SyncTransactionsUseCase,
    private val filterByCategory: FilterTransactionsByCategoryUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : CoroutineScope {
    private val job: Job = SupervisorJob()
    override val coroutineContext = job + dispatcherProvider.main
    private var observeJob: Job? = null

    private val _uiState = MutableStateFlow(TransactionUiState(isLoading = true))
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    init {
        observeTransactions()
    }

    /** Handles a UI event and updates the immutable state. */
    fun onEvent(event: TransactionEvent) {
        when (event) {
            TransactionEvent.Refresh -> refresh()
            is TransactionEvent.FilterByCategory -> {
                _uiState.value = _uiState.value.copy(selectedCategory = event.category, error = null)
                observeTransactions(event.category)
            }
            is TransactionEvent.SelectTransaction -> Unit
        }
    }

    /** Releases resources owned by the state holder. */
    fun clear() {
        cancel()
    }

    private fun observeTransactions(category: TransactionCategory? = _uiState.value.selectedCategory) {
        observeJob?.cancel()
        observeJob = launch(dispatcherProvider.io) {
            val source = category?.let { filterByCategory(it) } ?: getTransactions()
            source.collectLatest { transactions ->
                _uiState.value = _uiState.value.copy(
                    transactions = transactions,
                    isLoading = false,
                    isRefreshing = false,
                    error = null,
                )
            }
        }
    }

    private fun refresh() {
        launch(dispatcherProvider.io) {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            syncTransactions()
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        isLoading = false,
                        error = throwable.message,
                    )
                }
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isRefreshing = false, error = null)
                }
        }
    }
}

/** Immutable UI state exposed by [TransactionViewModel]. */
data class TransactionUiState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: TransactionCategory? = null,
    val isRefreshing: Boolean = false,
)

/** User-driven events supported by the transaction screen. */
sealed interface TransactionEvent {
    data object Refresh : TransactionEvent
    data class FilterByCategory(val category: TransactionCategory?) : TransactionEvent
    data class SelectTransaction(val id: String) : TransactionEvent
}
