package com.c242_ps302.sehatin.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.local.entity.PredictionEntity
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.repository.HealthRepository
import com.c242_ps302.sehatin.presentation.utils.collectAndHandle
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
                    it.copy(historyIsLoading = false, historyError = error)
                }
            },
            onLoading = {
                _historyState.update {
                    it.copy(historyIsLoading = true, historyError = null)
                }
            }
        ) { history ->
            _historyState.update {
                it.copy(historyIsLoading = false, historyError = null, history = history, historySuccess = true)
            }
        }
    }

    private fun fetchAllFoods() = viewModelScope.launch {
        repository.getAllPredictions().collectAndHandle(
            onError = { error ->
                _historyState.update {
                    it.copy(foodIsLoading = false, foodError = error)
                }
            },
            onLoading = {
                _historyState.update {
                    it.copy(foodIsLoading = true, foodError = null)
                }
            }
        ) { foods ->
            _historyState.update {
                it.copy(foodIsLoading = false, foodError = null, foods = foods, foodSuccess = true)
            }
        }
    }

    fun clearError() {
        _historyState.update {
            it.copy(foodError = null, historyError = null)
        }
    }

    fun clearSuccess() {
        _historyState.update {
            it.copy(foodSuccess = false, historySuccess = false)
        }
    }
}

data class HistoryScreenUiState(
    val history: List<RecommendationEntity> = emptyList(),
    val foods: List<PredictionEntity> = emptyList(),
    val historyIsLoading: Boolean = false,
    val foodIsLoading: Boolean = false,
    val foodError: String? = null,
    val historyError: String? = null,
    val foodSuccess: Boolean = false,
    val historySuccess: Boolean = false,
)