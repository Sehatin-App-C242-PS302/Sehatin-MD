package com.c242_ps302.sehatin.data.remote.dto.news_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    @SerialName("articles")
    val articles: List<ArticleDto>,
    @SerialName("status")
    val status: String,
    @SerialName("totalResults")
    val totalResults: Int,
)

@Serializable
data class SourceDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
)

@Serializable
data class ArticleDto(
    @SerialName("author")
    val author: String,
    @SerialName("content")
    val content: String,
    @SerialName("description")
    val description: String,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("source")
    val source: SourceDto,
    @SerialName("title")
    val title: String,
    @SerialName("url")
    val url: String,
    @SerialName("urlToImage")
    val urlToImage: String,
)