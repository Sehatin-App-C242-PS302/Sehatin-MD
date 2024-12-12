package com.c242_ps302.sehatin.ui.screen.home

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
class HomeViewModel @Inject constructor(
    private val repository: HealthRepository,
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeScreenUIState())
    val homeState = _homeState.asStateFlow()

    init {
        fetchLatestRecommendation()
        fetchLatestPrediction()
    }

    private fun fetchLatestRecommendation() = viewModelScope.launch {
        repository.getLatestRecommendationByUserId().collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { result ->
            _homeState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    latestRecommendation = result,
                    success = result != null
                )
            }
        }
    }

    private fun fetchLatestPrediction() = viewModelScope.launch {
        repository.getLatestPrediction().collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { result ->
            _homeState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    latestPrediction = result,
                    success = result != null
                )
            }
        }
    }


    fun clearError() {
        _homeState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _homeState.update {
            it.copy(success = false)
        }
    }
}

data class HomeScreenUIState(
    val latestRecommendation: RecommendationEntity? = null,
    val latestPrediction: PredictionEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)