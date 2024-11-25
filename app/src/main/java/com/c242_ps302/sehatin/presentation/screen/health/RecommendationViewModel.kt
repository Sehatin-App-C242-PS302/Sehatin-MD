package com.c242_ps302.sehatin.presentation.screen.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.repository.RecommendationRepository
import com.c242_ps302.sehatin.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val recommendationRepository: RecommendationRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RecommendationScreenUIState())
    val uiState = _uiState.asStateFlow()

    private val _recommendation = MutableStateFlow<RecommendationEntity?>(null)
    val recommendation = _recommendation.asStateFlow()

    fun getRecommendation(gender: String, age: Int, height: Double, weight: Double) {
        viewModelScope.launch {
            recommendationRepository.getRecommendationFromApiAndSave(gender, age, height, weight).collect { result ->
                when (result) {
                    Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null,
                        )
                        _recommendation.value = result.data
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.error,
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class RecommendationScreenUIState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
)
