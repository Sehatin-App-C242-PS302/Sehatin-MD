package com.c242_ps302.sehatin.presentation.screen.health.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ResultViewModel @Inject constructor(
    private val repository: HealthRepository,
) : ViewModel() {

    private val _healthResultState = MutableStateFlow(HealthResultScreenUIState())
    val healthResultState = _healthResultState.asStateFlow()

    init {
        fetchLatestRecommendation()
    }

    private fun fetchLatestRecommendation() = viewModelScope.launch {
        repository.getLatestRecommendationByUserId().collectAndHandle(
            onError = { error ->
                _healthResultState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _healthResultState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { result ->
            _healthResultState.update {
                it.copy(isLoading = false, error = null, result = result)
            }
        }
    }

}

data class HealthResultScreenUIState(
    val result: RecommendationEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
