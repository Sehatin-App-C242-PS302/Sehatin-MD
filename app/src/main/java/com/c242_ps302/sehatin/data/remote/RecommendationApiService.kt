package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.RecommendationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RecommendationApiService {

    @FormUrlEncoded
    @POST("calculate_bmi")
    suspend fun getRecommendation(
        @Field("gender") gender: String,
        @Field("age") age: Int,
        @Field("height") height: Double,
        @Field("weight") weight: Double,
    ) : RecommendationResponse
}