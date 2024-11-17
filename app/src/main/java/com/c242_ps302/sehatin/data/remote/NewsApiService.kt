package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.dto.news_dto.NewsDto
import com.c242_ps302.sehatin.data.utils.Constants.NEWS_API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getHeadlineNews(
        @Query("q") query: String,
    ): NewsDto

}