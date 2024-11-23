package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.mapper.toNews
import com.c242_ps302.sehatin.data.remote.NewsApiService
import com.c242_ps302.sehatin.data.remote.response.ArticlesItem
import com.c242_ps302.sehatin.domain.model.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class NewsRepository(
    private val newsApiService: NewsApiService,
) {
    fun getSearchedNews(query: String = "health"): Flow<Result<List<News>>> = flow {
        emit(Result.Loading)
        try {
            val response = newsApiService.getHeadlineNews(query = query)
            val news = response.articles

            if (news.isNullOrEmpty()) {
                emit(Result.Error("No data found"))
            } else {
                try {
                    val newsList = news
                        .filterNot { article ->
                            article?.title == "[Removed]" ||
                                    article?.description == "[Removed]" ||
                                    article?.content == "[Removed]"
                        }
                        .mapNotNull { article: ArticlesItem? ->
                            article?.toNews()
                        }
                    if (newsList.isEmpty()) {
                        emit(Result.Error("No valid news found"))
                    } else {
                        emit(Result.Success(newsList))
                    }
                } catch (e: Exception) {
                    emit(Result.Error("Error: ${e.message}"))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getDetailNews(query: String): Flow<Result<News>> = flow {
        emit(Result.Loading)
        try {
            val response = newsApiService.getHeadlineNews(query = query)
            val news = response.articles

            if (news.isNullOrEmpty()) {
                emit(Result.Error("No data found"))
            } else {
                try {
                    val newsDetail = news.firstOrNull { article ->
                        article?.title?.contains(query, ignoreCase = true) == true &&
                                article.title != "[Removed]" &&
                                article.description != "[Removed]" &&
                                article.content != "[Removed]"
                    }

                    if (newsDetail == null) {
                        emit(Result.Error("News not found"))
                    } else {
                        val mappedNews = newsDetail.toNews()
                        emit(Result.Success(mappedNews))
                    }
                } catch (e: Exception) {
                    emit(Result.Error("Error mapping news: ${e.message}"))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error("Error fetching news: ${e.message}"))
        }
    }
}
