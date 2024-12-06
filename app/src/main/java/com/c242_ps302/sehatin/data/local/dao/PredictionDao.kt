package com.c242_ps302.sehatin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.c242_ps302.sehatin.data.local.entity.PredictionEntity

@Dao
interface PredictionDao {
    @Query("SELECT * FROM prediction_data")
    suspend fun getAllPredictions(): List<PredictionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPredictions(predictions: List<PredictionEntity>)

    @Update
    suspend fun updatePrediction(prediction: PredictionEntity)

    @Query("SELECT * FROM prediction_data ORDER BY createdAt DESC LIMIT 1")
    suspend fun getCurrentPrediction(): PredictionEntity

    @Query("DELETE FROM prediction_data")
    suspend fun clearAllPredictions()
}