package com.c242_ps302.sehatin.presentation.screen.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.repository.NewsRepository
import com.c242_ps302.sehatin.data.repository.Result
import com.c242_ps302.sehatin.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _newsList = MutableStateFlow<List<News>>(emptyList())
    val newsList = _newsList.asStateFlow()

    init {
        getHeadlineNews()
    }

    private fun getHeadlineNews() {
        viewModelScope.launch {
            newsRepository.getAllNews().collect { result ->
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
                        _newsList.value = result.data
                    }
                }
            }
        }
    }

    fun refreshNews() {
        getHeadlineNews()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class NewsScreenUiState(
    val isLoading: Boolean = true,
    val success: Boolean = false,
    val news: News? = null,
    val error: String? = null,
)