package com.c242_ps302.sehatin.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.repository.HistoryRepository
import com.c242_ps302.sehatin.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _historyList = MutableStateFlow<List<RecommendationEntity>>(emptyList())
    val historyList = _historyList.asStateFlow()

    fun getAllHistory() {
        viewModelScope.launch {
            historyRepository.getAllRecommendation().collect { result ->
                when (result) {
                    Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.error,
                        )
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null,
                            success = true,
                        )
                        _historyList.value = result.data
                    }
                }
            }
        }
    }
}

data class HistoryScreenUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)