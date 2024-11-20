package com.c242_ps302.sehatin.data.remote.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem?>? = null,
)

data class Source(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("name")
	val name: String? = null,
)

data class ArticlesItem(

	@field:SerializedName("source")
	val source: Source? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("title")
	val title: String? = null,
	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("urlToImage")
	val urlToImage: String? = null,

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("content")
	val content: String? = null,
)
