package com.c242_ps302.sehatin.data.mapper

import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.local.entity.UserEntity
import com.c242_ps302.sehatin.data.remote.response.ArticlesItem
import com.c242_ps302.sehatin.data.remote.response.RecommendationResponse
import com.c242_ps302.sehatin.data.remote.response.User
import com.c242_ps302.sehatin.data.utils.formatCardDate
import com.c242_ps302.sehatin.domain.model.News

fun ArticlesItem.toNews(): News {
    return News(
        source = this.source?.name ?: "Unknown Source",
        author = this.author?.take(20) ?: "Anonymous",
        title = this.title ?: "No Title",
        description = this.description ?: "No Description",
        url = this.url ?: "",
        urlToImage = this.urlToImage ?: "",
        publishedAt = this.publishedAt?.let { formatCardDate(it) } ?: "Unknown Date",
        content = this.content ?: "No Content"
    )
}

fun RecommendationResponse.toEntity(): RecommendationEntity {
    return RecommendationEntity(
        gender = this.gender,
        weightKg = this.weightKg,
        heightCm = this.heightCm,
        category = this.category,
        age = this.age,
        bmi = this.bmi,
        dailyStepRecommendation = this.dailyStepRecommendation,
        createdAt = System.currentTimeMillis().toString()
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        password = this.password,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}


