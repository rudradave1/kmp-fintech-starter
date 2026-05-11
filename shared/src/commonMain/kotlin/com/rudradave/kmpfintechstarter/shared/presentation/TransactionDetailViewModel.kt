package com.rudradave.kmpfintechstarter.shared.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.rudradave.kmpfintechstarter.shared.domain.model.Transaction
import com.rudradave.kmpfintechstarter.shared.domain.usecase.GetTransactionByIdUseCase
import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/** Shared state holder for the transaction detail experience. */
class TransactionDetailViewModel(
    private val transactionId: String,
    private val getTransactionById: GetTransactionByIdUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ScreenModel {

    private val _uiState = MutableStateFlow(TransactionDetailUiState(isLoading = true))
    val uiState: StateFlow<TransactionDetailUiState> = _uiState.asStateFlow()

    init {
        screenModelScope.launch(dispatcherProvider.io) {
            getTransactionById(transactionId).collectLatest { transaction ->
                _uiState.value = _uiState.value.copy(
                    transaction = transaction,
                    isLoading = false,
                )
            }
        }
    }
}

/** Immutable UI state exposed by [TransactionDetailViewModel]. */
data class TransactionDetailUiState(
    val transaction: Transaction? = null,
    val isLoading: Boolean = false,
)
