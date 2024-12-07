package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.GetPredictionResponse
import com.c242_ps302.sehatin.data.remote.response.GetRecommendationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HealthApiService {
    @GET("predictions/user/{userId}")
    suspend fun getRecommendationByUserId(
        @Path("userId") userId: Int
    ) : GetRecommendationResponse

    @GET("predict/image")
    suspend fun getPredictionByUser() : GetPredictionResponse
}