package com.c242_ps302.sehatin.data.mapper

import android.content.Context
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.data.local.entity.UserEntity
import com.c242_ps302.sehatin.data.remote.response.ArticlesItem
import com.c242_ps302.sehatin.data.remote.response.HealthResponse
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

fun HealthResponse.toEntity(context: Context): List<RecommendationEntity> {
    return data?.mapNotNull { dataItem ->
        dataItem?.let {
            RecommendationEntity(
                gender = it.gender ?: "",
                age = it.age ?: 0,
                heightCm = it.height?.toDouble() ?: 0.0,
                weightKg = it.weight?.toDouble() ?: 0.0,
                bmi = (it.bmi as? Number)?.toDouble() ?: 0.0,
                category = determineBMICategory(context, (it.bmi as? Number)?.toDouble() ?: 0.0),
                dailyStepRecommendation = it.recommendedSteps?.toString(),
                createdAt = it.createdAt ?: System.currentTimeMillis().toString()
            )
        }
    } ?: emptyList()
}

fun determineBMICategory(context: Context, bmi: Double): String {
    return when {
        bmi < 18.5 -> context.getString(R.string.underweight)
        bmi in 18.5..24.9 -> context.getString(R.string.normal_weight)
        bmi in 25.0..29.9 -> context.getString(R.string.overweight)
        bmi >= 30.0 -> context.getString(R.string.obese)
        else -> context.getString(R.string.unknown)
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


