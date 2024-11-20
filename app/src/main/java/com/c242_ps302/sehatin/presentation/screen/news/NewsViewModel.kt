package com.c242_ps302.sehatin.presentation.screen.news

import androidx.lifecycle.ViewModel
import com.c242_ps302.sehatin.data.repository.NewsRepository
import com.c242_ps302.sehatin.data.repository.Result
import com.c242_ps302.sehatin.domain.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _newsList = MutableStateFlow<List<News>>(emptyList())
    val newsList = _newsList.asStateFlow()

    fun getHeadlineNews() {
        newsRepository.getAllNews().observeForever { result ->
            when(result) {
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error,
                        news = null
                    )
                }
                Result.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        news = result.data
                    )
                }
            }
        }
    }
}

data class NewsScreenUiState(
    val isLoading: Boolean = true,
    val news: List<News>? = null,
    val error: String? = null,
)