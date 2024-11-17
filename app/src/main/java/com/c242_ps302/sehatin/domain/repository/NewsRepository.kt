package com.c242_ps302.sehatin.domain.repository

import com.c242_ps302.sehatin.domain.model.News

interface NewsRepository {
    suspend fun getHeadlineNews(query: String): List<News>
}