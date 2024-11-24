package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.RecommendationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HealthApiService {
    @GET("health/user/{id}")
    suspend fun getUserHealthData(
        @Path("id") userId: Int
    ): RecommendationResponse

    @GET("health/{id}")
    suspend fun getSingleHealthData(
        @Path("id") healthId: Int
    ): RecommendationResponse
}