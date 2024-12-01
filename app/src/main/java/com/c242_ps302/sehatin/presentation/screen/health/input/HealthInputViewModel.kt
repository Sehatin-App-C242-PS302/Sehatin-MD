package com.c242_ps302.sehatin.presentation.screen.health.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.repository.RecommendationRepository
import com.c242_ps302.sehatin.presentation.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val repository: RecommendationRepository,
) : ViewModel() {

    private val _healthInputState = MutableStateFlow(HealthInputScreenUIState())
    val healthInputState = _healthInputState.asStateFlow()

    fun postRecommendation(gender: String, age: Int, height: Double, weight: Double) =
        viewModelScope.launch {
            repository.postRecommendationAndSave(gender, age, height, weight).collectAndHandle(
                onError = { error ->
                    _healthInputState.update {
                        it.copy(isLoading = false, error = error)
                    }
                },
                onLoading = {
                    _healthInputState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
            ) {
                _healthInputState.update {
                    it.copy(isLoading = false, error = null, success = true)
                }
            }
        }

}

data class HealthInputScreenUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)
