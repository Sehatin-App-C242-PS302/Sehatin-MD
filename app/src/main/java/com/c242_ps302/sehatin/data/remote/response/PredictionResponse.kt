package com.c242_ps302.sehatin.data.remote.response

import com.google.gson.annotations.SerializedName

data class PostPredictionResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class GetPredictionResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("data")
	val data: List<PredictionData?>? = null,
)

data class PredictionData(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("predicted_class")
	val predictedClass: String? = null,

	@field:SerializedName("calories")
	val calories: Double? = null,

	@field:SerializedName("protein")
	val protein: Double? = null,

	@field:SerializedName("fat")
	val fat: Double? = null,

	@field:SerializedName("carbohydrates")
	val carbohydrates: Double? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,
)
