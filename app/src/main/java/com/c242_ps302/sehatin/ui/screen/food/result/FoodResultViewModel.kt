package com.c242_ps302.sehatin.ui.screen.food.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.local.entity.PredictionEntity
import com.c242_ps302.sehatin.data.repository.HealthRepository
import com.c242_ps302.sehatin.ui.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodResultViewModel @Inject constructor(
    private val repository: HealthRepository
) : ViewModel() {
    private val _foodResultState = MutableStateFlow(FoodResultScreenUIState())
    val foodResultState = _foodResultState.asStateFlow()

    init {
        fetchLatestPrediction()
    }

    private fun fetchLatestPrediction() = viewModelScope.launch {
        repository.getLatestPrediction().collectAndHandle(
            onError = { error ->
                _foodResultState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _foodResultState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { result ->
            _foodResultState.update {
                it.copy(isLoading = false, error = null, result = result, success = true)
            }
        }
    }

    fun clearError() {
        _foodResultState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _foodResultState.update {
            it.copy(result = null)
        }
    }
}

data class FoodResultScreenUIState(
    val result: PredictionEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)