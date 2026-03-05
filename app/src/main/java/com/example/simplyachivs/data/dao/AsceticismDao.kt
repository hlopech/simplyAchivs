package com.example.simplyachivs.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplyachivs.data.entity.AsceticismCheckInEntity
import com.example.simplyachivs.data.entity.AsceticismEntity

@Dao
interface AsceticismDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AsceticismEntity)

    @Update
    suspend fun update(entity: AsceticismEntity)

    @Query("SELECT * FROM ascetisms WHERE userId = :userId AND status = 'ACTIVE' ORDER BY createdAt DESC")
    suspend fun getActive(userId: String): List<AsceticismEntity>

    @Query("SELECT * FROM ascetisms WHERE userId = :userId AND status != 'ACTIVE' ORDER BY createdAt DESC")
    suspend fun getHistory(userId: String): List<AsceticismEntity>

    @Query("SELECT * FROM asceticism_checkins WHERE asceticismId = :id ORDER BY epochDay ASC")
    suspend fun getCheckIns(id: String): List<AsceticismCheckInEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCheckIn(entity: AsceticismCheckInEntity)
}
