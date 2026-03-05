package com.example.simplyachivs.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simplyachivs.data.entity.EventEntity
import com.example.simplyachivs.domain.model.event.EventType
import java.util.UUID

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: EventEntity)

    @Query("SELECT * FROM events WHERE userId = :userId ORDER BY createdAt ASC")
    suspend fun getAllByUser(userId: UUID): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events WHERE userId = :userId AND type = :type")
    suspend fun countByType(userId: UUID, type: EventType): Int
}
