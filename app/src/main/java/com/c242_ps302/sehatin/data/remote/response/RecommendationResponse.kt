package com.c242_ps302.sehatin.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,
)

data class Data(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("height")
	val height: Double? = null,

	@field:SerializedName("weight")
	val weight: Double? = null,

	@field:SerializedName("bmi")
	val bmi: Double? = null,

	@field:SerializedName("recommended_steps")
	val recommendedSteps: Int? = null,
)
