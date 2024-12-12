package com.c242_ps302.sehatin.ui.screen.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.repository.NewsRepository
import com.c242_ps302.sehatin.domain.model.News
import com.c242_ps302.sehatin.ui.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _newsState = MutableStateFlow(NewsScreenUiState())
    val newsState = _newsState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        fetchHeadlineNews()
    }

    private fun fetchHeadlineNews(query: String? = null) = viewModelScope.launch {
        newsRepository.getSearchedNews(query ?: "health").collectAndHandle(
            onError = { error ->
                _newsState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _newsState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { news ->
            _newsState.update {
                it.copy(isLoading = false, error = null, news = news, success = true)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        fetchHeadlineNews(query.ifBlank { "health" })
    }

    fun refreshNews() {
        fetchHeadlineNews()
    }

    fun clearError() {
        _newsState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _newsState.update {
            it.copy(success = false)
        }
    }
}

data class NewsScreenUiState(
    val news: List<News> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)
