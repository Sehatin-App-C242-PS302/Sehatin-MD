package com.c242_ps302.sehatin.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
