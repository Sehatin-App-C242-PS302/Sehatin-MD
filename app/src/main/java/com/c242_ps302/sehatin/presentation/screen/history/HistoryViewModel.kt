package com.c242_ps302.sehatin.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.repository.HistoryRepository
import com.c242_ps302.sehatin.presentation.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository,
) : ViewModel() {
    private val _historyState = MutableStateFlow(HistoryScreenUiState())
    val historyState = _historyState.asStateFlow()

    init {
        fetchAllHistory()
    }

    private fun fetchAllHistory() = viewModelScope.launch {
        repository.getAllRecommendation().collectAndHandle(
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
                it.copy(isLoading = false, error = null, history = history)
            }
        }
    }
}

data class HistoryScreenUiState(
    val history: List<RecommendationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)