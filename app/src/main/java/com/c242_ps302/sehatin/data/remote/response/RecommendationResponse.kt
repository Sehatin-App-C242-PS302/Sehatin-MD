package com.c242_ps302.sehatin.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
    val id: Int = 0,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("age")
    val age: Int? = null,

    @field:SerializedName("height_cm")
    val heightCm: Double? = null,

    @field:SerializedName("weight_kg")
    val weightKg: Double? = null,

    @field:SerializedName("bmi")
    val bmi: Double? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("daily_step_recommendation")
    val dailyStepRecommendation: String? = null,
)
