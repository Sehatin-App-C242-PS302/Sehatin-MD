package com.c242_ps302.sehatin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendation_data")
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gender: String? = null,
    val weightKg: Double? = null,
    val heightCm: Double? = null,
    val category: String? = null,
    val age: Int? = null,
    val bmi: Double? = null,
    val dailyStepRecommendation: String? = null,
    val createdAt: String = System.currentTimeMillis().toString()

)
