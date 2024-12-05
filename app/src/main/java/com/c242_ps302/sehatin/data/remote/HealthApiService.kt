package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.HealthResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HealthApiService {
    @GET("predictions/user/{userId}")
    suspend fun getRecommendationByUserId(
        @Path("userId") userId: Int
    ) : HealthResponse
}