package com.c242_ps302.sehatin.domain.model

data class News(
    val title: String,
    val description: String,
    val content: String,
    val source: String,
    val url: String,
    val imageUrl: String,
    val author: String,
    val publishedAt: String,
)
