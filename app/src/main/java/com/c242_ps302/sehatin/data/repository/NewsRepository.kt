package com.c242_ps302.sehatin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.c242_ps302.sehatin.data.mapper.toNews
import com.c242_ps302.sehatin.data.remote.NewsApiService
import com.c242_ps302.sehatin.data.remote.response.ArticlesItem
import com.c242_ps302.sehatin.domain.model.News


class NewsRepository(
    private val newsApiService: NewsApiService,
) {
    fun getAllNews(): LiveData<Result<List<News>>> = liveData {
        emit(Result.Loading)
        try {
            val response = newsApiService.getHeadlineNews("health")
            val news = response.articles

            if (news.isNullOrEmpty()) {
                emit(Result.Error("No data found"))
            } else {
                try {
                    val newsList = news.mapNotNull { article: ArticlesItem? ->
                        article?.toNews()
                    }
                    emit(Result.Success(newsList))
                } catch (e: Exception) {
                    emit(Result.Error("Error: ${e.message}"))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }
}