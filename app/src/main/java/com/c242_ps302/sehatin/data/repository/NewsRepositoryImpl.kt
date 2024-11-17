package com.c242_ps302.sehatin.data.repository

import com.c242_ps302.sehatin.data.mapper.toDomainModelList
import com.c242_ps302.sehatin.data.remote.NewsApiService
import com.c242_ps302.sehatin.domain.model.News
import com.c242_ps302.sehatin.domain.repository.NewsRepository


class NewsRepositoryImpl(private val newsApiService: NewsApiService) : NewsRepository {
    override suspend fun getHeadlineNews(query: String): List<News> {
        return newsApiService.getHeadlineNews(query).toDomainModelList()
    }

}