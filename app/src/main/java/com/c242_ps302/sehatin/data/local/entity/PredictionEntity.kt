package com.c242_ps302.sehatin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_data")
data class PredictionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId: String? = null,
    val imageUrl: String? = null,
    val predictedClass: String? = null,
    val calories: Double? = null,
    val protein: Double? = null,
    val fat: Double? = null,
    val carbohydrates: Double? = null,
    val createdAt: String = System.currentTimeMillis().toString()
)
