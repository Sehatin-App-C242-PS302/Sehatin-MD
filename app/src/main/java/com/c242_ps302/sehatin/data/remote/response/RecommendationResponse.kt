package com.c242_ps302.sehatin.data.remote.response

import com.google.gson.annotations.SerializedName

data class PostRecommendationResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,
)

data class GetRecommendationResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("data")
    val data: List<RecommendationData?>? = null,
)

data class RecommendationData(

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("age")
    val age: Int? = null,

    @field:SerializedName("height")
    val height: Int? = null,

    @field:SerializedName("weight")
    val weight: Int? = null,

    @field:SerializedName("bmi")
    val bmi: Any? = null,

    @field:SerializedName("recommended_steps")
    val recommendedSteps: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,
)
