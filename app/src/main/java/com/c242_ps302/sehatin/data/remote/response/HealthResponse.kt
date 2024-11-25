package com.c242_ps302.sehatin.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetHealthResponse(
	@field:SerializedName("GetHealthResponse")
	val getHealthResponse: List<HealthResponseItem?>? = null
)

data class HealthResponseItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("bmi")
	val bmi: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

