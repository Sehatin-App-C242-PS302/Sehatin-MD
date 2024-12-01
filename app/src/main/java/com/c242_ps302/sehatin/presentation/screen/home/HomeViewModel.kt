package com.c242_ps302.sehatin.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.repository.RecommendationRepository
import com.c242_ps302.sehatin.presentation.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecommendationRepository,
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeScreenUIState())
    val homeState = _homeState.asStateFlow()

    init {
        fetchLatestRecommendation()
        fetchAllRecommendations()
    }


    private fun fetchAllRecommendations() = viewModelScope.launch {
        repository.getAllRecommendation().collectAndHandle(
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
                it.copy(isLoading = false, error = null, recommendationsList = result)
            }
        }
    }

    private fun fetchLatestRecommendation() = viewModelScope.launch {
        repository.getLatestRecommendation().collectAndHandle(
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
                it.copy(isLoading = false, error = null, latestRecommendation = result)
            }
        }
    }
}

data class HomeScreenUIState(
    val recommendationsList: List<RecommendationEntity> = emptyList(),
    val latestRecommendation: RecommendationEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)