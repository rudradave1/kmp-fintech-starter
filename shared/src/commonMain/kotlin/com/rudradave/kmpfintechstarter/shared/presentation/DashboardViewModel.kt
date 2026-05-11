package com.rudradave.kmpfintechstarter.shared.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.rudradave.kmpfintechstarter.shared.domain.model.DashboardState
import com.rudradave.kmpfintechstarter.shared.domain.usecase.GetDashboardDataUseCase
import com.rudradave.kmpfintechstarter.shared.domain.usecase.SyncDashboardDataUseCase
import com.rudradave.kmpfintechstarter.shared.platform.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/** Shared state holder for the dashboard experience. */
class DashboardViewModel(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val syncDashboardDataUseCase: SyncDashboardDataUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ScreenModel {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        screenModelScope.launch(dispatcherProvider.io) {
            getDashboardDataUseCase().collectLatest { dashboardState ->
                _uiState.value = _uiState.value.copy(
                    dashboardState = dashboardState,
                    isLoading = false,
                    isRefreshing = false,
                    error = null,
                )
            }
        }
    }

    /** Refreshes both account and transaction data while keeping cached UI visible. */
    fun refresh() {
        screenModelScope.launch(dispatcherProvider.io) {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            syncDashboardDataUseCase()
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

/** Immutable UI state exposed by [DashboardViewModel]. */
data class DashboardUiState(
    val dashboardState: DashboardState? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
)
