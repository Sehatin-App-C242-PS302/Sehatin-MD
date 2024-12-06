package com.c242_ps302.sehatin.data.remote

import com.c242_ps302.sehatin.data.remote.response.PostRecommendationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RecommendationApiService {

    @FormUrlEncoded
    @POST("process-data")
    suspend fun postRecommendation(
        @Field("user_id") userId: Int,
        @Field("gender") gender: String,
        @Field("age") age: Int,
        @Field("height") height: Double,
        @Field("weight") weight: Double,
    ) : PostRecommendationResponse

}