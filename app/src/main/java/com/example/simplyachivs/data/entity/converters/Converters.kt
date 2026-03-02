package com.example.simplyachivs.data.entity.converters

import androidx.room.TypeConverter
import com.example.simplyachivs.domain.model.award.AwardStatus
import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import com.example.simplyachivs.domain.model.goal.GoalStatus
import com.example.simplyachivs.domain.model.goal.StepStatus
import java.time.Instant
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromTaskComplexity(value: TaskComplexity): String = value.name

    @TypeConverter
    fun toTaskComplexity(value: String): TaskComplexity = TaskComplexity.valueOf(value)

    @TypeConverter
    fun fromGoalComplexity(value: GoalComplexity): String = value.name

    @TypeConverter
    fun toGoalComplexity(value: String): GoalComplexity = GoalComplexity.valueOf(value)

    @TypeConverter
    fun fromGoalStatus(value: GoalStatus): String = value.name

    @TypeConverter
    fun toGoalStatus(value: String): GoalStatus = GoalStatus.valueOf(value)

    @TypeConverter
    fun fromStepStatus(value: StepStatus): String = value.name

    @TypeConverter
    fun toStepStatus(value: String): StepStatus = StepStatus.valueOf(value)
    @TypeConverter

    fun fromAwardStatus(value: AwardStatus): String = value.name

    @TypeConverter
    fun toAwardStatus(value: String): AwardStatus = AwardStatus.valueOf(value)

    @TypeConverter
    fun fromInstant(value: Instant): Long = value.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long): Instant = Instant.ofEpochMilli(value)

    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toUUID(uuid: String): UUID = UUID.fromString(uuid)


}

