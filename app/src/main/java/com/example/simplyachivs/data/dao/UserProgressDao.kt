package com.example.simplyachivs.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplyachivs.data.entity.UserEntity
import com.example.simplyachivs.data.entity.UserProgressEntity
import java.util.UUID

@Dao
interface UserProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProgress(userProgress: UserProgressEntity)

    @Delete
    suspend fun deleteUserProgress(userProgress: UserProgressEntity)


    @Update()
    suspend fun updateUserProgress(userProgress: UserProgressEntity)

    @Query("SELECT * FROM userProgress WHERE userId = :userId ")
    suspend fun getUserProgress(userId: UUID): UserProgressEntity?


}