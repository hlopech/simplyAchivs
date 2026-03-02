package com.example.simplyachivs.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplyachivs.data.entity.AwardEntity
import com.example.simplyachivs.data.entity.GoalEntity
import com.example.simplyachivs.data.entity.StepEntity
import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.model.goal.Step
import java.util.UUID

@Dao
interface AwardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAward(award: AwardEntity)

    @Delete
    suspend fun deleteAward(award: AwardEntity)

    @Update
    suspend fun updateAward(award: AwardEntity)

    @Query("SELECT * FROM awards WHERE id = :awardId ")
    suspend fun getAward(awardId: UUID): AwardEntity?


    @Query("SELECT * FROM awards WHERE userId = :userId ")
    suspend fun getAllAwards(userId: UUID): List<AwardEntity>

    @Query("SELECT * FROM awards WHERE userId = :userId AND status ='ACTIVE'")
    suspend fun getActiveAwards(userId: UUID): List<AwardEntity>

    @Query("SELECT * FROM awards WHERE userId = :userId AND status ='COMPLETED'")
    suspend fun getCompletedAwards(userId: UUID): List<AwardEntity>


}