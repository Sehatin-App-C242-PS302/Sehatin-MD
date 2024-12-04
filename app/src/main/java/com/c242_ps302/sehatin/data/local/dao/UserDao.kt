package com.c242_ps302.sehatin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.c242_ps302.sehatin.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user_data LIMIT 1")
    suspend fun getUserData(): UserEntity

    @Query("DELETE FROM user_data")
    suspend fun clearUserData()
}