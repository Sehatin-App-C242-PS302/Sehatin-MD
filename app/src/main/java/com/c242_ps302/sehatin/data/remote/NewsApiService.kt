package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getHeadlineNews(
        @Query("q") query: String,
    ): NewsResponse

}