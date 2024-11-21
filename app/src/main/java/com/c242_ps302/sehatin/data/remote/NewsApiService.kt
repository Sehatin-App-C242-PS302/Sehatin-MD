package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("everything")
    suspend fun getHeadlineNews(
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("searchIn") searchIn: String = "title",
        @Query("q") query: String,
    ): NewsResponse

}