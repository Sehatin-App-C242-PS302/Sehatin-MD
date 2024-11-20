package com.c242_ps302.sehatin.data.mapper

import com.c242_ps302.sehatin.data.remote.response.ArticlesItem
import com.c242_ps302.sehatin.domain.model.News

fun ArticlesItem.toNews(): News {
    return News(
        source = this.source?.name ?: "Unknown Source",
        author = this.author ?: "Unknown Author",
        title = this.title ?: "No Title",
        description = this.description ?: "No Description",
        url = this.url ?: "",
        urlToImage = this.urlToImage ?: "",
        publishedAt = this.publishedAt ?: "Unknown Date",
        content = this.content ?: "No Content"
    )
}


