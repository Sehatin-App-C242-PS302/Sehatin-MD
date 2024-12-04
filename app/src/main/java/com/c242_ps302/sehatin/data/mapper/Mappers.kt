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

fun RecommendationResponse.toEntity(): List<RecommendationEntity> {
    return data?.let { dataItem ->
        listOf(
            RecommendationEntity(
                gender = dataItem.gender,
                age = dataItem.age,
                heightCm = dataItem.height ?: 0.0,
                weightKg = dataItem.weight ?: 0.0,
                bmi = dataItem.bmi ?: 0.0,
                category = determineBMICategory(dataItem.bmi ?: 0.0),
                dailyStepRecommendation = dataItem.recommendedSteps?.toString(),
                createdAt = System.currentTimeMillis().toString() // Ensure createdAt is set here
            )
        ) ?: emptyList()
    } ?: emptyList()
}

fun determineBMICategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi in 18.5..24.9 -> "Normal weight"
        bmi in 25.0..29.9 -> "Overweight"
        bmi >= 30.0 -> "Obese"
        else -> "Unknown"
    }
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


