package com.c242_ps302.sehatin.presentation.screen.news

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.domain.model.News
import com.c242_ps302.sehatin.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    var news: List<News> by mutableStateOf(emptyList())

    init {
        getNews()
    }

    private fun getNews() {
        viewModelScope.launch {
            try {
                val result = repository.getHeadlineNews("health")
                news = result
            } catch (e: Exception) {
                Log.e(TAG, "getNews: ${e.message}", )
            }
        }
    }

    companion object {
        private const val TAG = "NewsViewModel"
    }
}