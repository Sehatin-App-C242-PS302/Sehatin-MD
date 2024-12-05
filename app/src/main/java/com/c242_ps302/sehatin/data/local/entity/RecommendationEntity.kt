package com.c242_ps302.sehatin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendation_data")
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val gender: String? = null,
    val age: Int? = null,
    val heightCm: Double? = null,
    val weightKg: Double? = null,
    val bmi: Double? = null,
    val category: String? = null,
    val dailyStepRecommendation: String? = null,
    val createdAt: String = System.currentTimeMillis().toString()
)
