package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.RecommendationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HealthApiService {
    @GET("predictions/user/{user_id}")
    suspend fun getRecommendationByUserId(
        @Path("user_id") userId: Int
    ) : RecommendationResponse
}