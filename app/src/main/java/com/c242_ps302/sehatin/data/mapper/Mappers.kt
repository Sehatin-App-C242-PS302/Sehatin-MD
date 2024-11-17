package com.c242_ps302.sehatin.data.mapper

import com.c242_ps302.sehatin.data.remote.dto.news_dto.ArticleDto
import com.c242_ps302.sehatin.data.remote.dto.news_dto.NewsDto
import com.c242_ps302.sehatin.domain.model.News

fun NewsDto.toDomainModelList(): List<News> {
    return this.articles.map { it.toDomainModel() }
}

fun ArticleDto.toDomainModel(): News {
    return News(
        title = title,
        description = description,
        content = content,
        source = source.name,
        url = url,
        imageUrl = urlToImage,
        author = author,
        publishedAt = publishedAt
    )
}

