package com.c242_ps302.sehatin.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.local.entity.PredictionEntity
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.repository.HealthRepository
import com.c242_ps302.sehatin.ui.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HealthRepository,
) : ViewModel() {
    private val _historyState = MutableStateFlow(HistoryScreenUiState())
    val historyState = _historyState.asStateFlow()

    init {
        fetchAllHistory()
        fetchAllFoods()
    }

    private fun fetchAllHistory() = viewModelScope.launch {
        repository.getAllRecommendationsByUserId().collectAndHandle(
            onError = { error ->
                _historyState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _historyState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { history ->
            _historyState.update {
                it.copy(isLoading = false, error = null, history = history, success = true)
            }
        }
    }

    private fun fetchAllFoods() = viewModelScope.launch {
        repository.getAllPredictions().collectAndHandle(
            onError = { error ->
                _historyState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _historyState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { foods ->
            _historyState.update {
                it.copy(isLoading = false, error = null, foods = foods, success = true)
            }
        }
    }

    fun clearSuccess() {
        _historyState.update {
            it.copy(success = false)
        }
    }

    fun clearError() {
        _historyState.update {
            it.copy(error = null)
        }
    }
}

data class HistoryScreenUiState(
    val history: List<RecommendationEntity> = emptyList(),
    val foods: List<PredictionEntity> = emptyList(),
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
)