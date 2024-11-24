package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.RecommendationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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

    @FormUrlEncoded
    @POST
    suspend fun createHealthData(
        @Field("gender") gender: String,
        @Field("age") age: Int,
        @Field("height") height: Double,
        @Field("weight") weight: Double,
        @Field("userId") userId: Int
    )
}