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

    private var isDataFetched = false

    fun getHeadlineNews() {
        if (!isDataFetched) {
            viewModelScope.launch {
                newsRepository.getAllNews().collect { result ->
                    when (result) {
                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.error,
                            )
                            isDataFetched = false
                        }

                        Result.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
                        }

                        is Result.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                            )
                            _newsList.value = result.data
                            isDataFetched = true
                        }
                    }
                }
            }
        }
    }
}

data class NewsScreenUiState(
    val isLoading: Boolean = true,
    val news: News? = null,
    val error: String? = null,
)