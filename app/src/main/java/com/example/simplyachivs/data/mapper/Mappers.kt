package com.example.simplyachivs.data.mapper

import com.example.simplyachivs.data.entity.AwardEntity
import com.example.simplyachivs.data.entity.GoalEntity
import com.example.simplyachivs.data.entity.StepEntity
import com.example.simplyachivs.data.entity.TaskEntity
import com.example.simplyachivs.data.entity.UserEntity
import com.example.simplyachivs.data.entity.UserProgressEntity
import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.model.task.Task
import com.example.simplyachivs.domain.model.user.User
import com.example.simplyachivs.domain.model.user.UserProgress

fun UserProgress.toEntity(): UserProgressEntity = UserProgressEntity(
    id = id,
    userId = userId,
    xp = xp,
    coin = coin,
    streak = streak,
    updatedAt = updatedAt
)

fun UserProgressEntity.toDomain(): UserProgress = UserProgress(
    id = id,
    userId = userId,
    xp = xp,
    coin = coin,
    streak = streak,
    updatedAt = updatedAt
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    avatar = avatar,
    createdAt = createdAt,
    lastActiveAt = lastActiveAt
)

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    avatar = avatar,
    createdAt = createdAt,
    lastActiveAt = lastActiveAt
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id!!,
    userId = userId,
    name = name,
    createdAt = createdAt,
    complexity = complexity,
    completedAt = completedAt
)

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    userId = userId,
    name = name,
    createdAt = createdAt,
    complexity = complexity,
    completedAt = completedAt
)


fun Step.toEntity(): StepEntity = StepEntity(
    id = id,
    goalId = goalId,
    name = name,
    status = status,
    position = position
)

fun StepEntity.toDomain(): Step = Step(
    id = id,
    goalId = goalId,
    name = name,
    status = status,
    position = position
)

fun Goal.toEntity(): GoalEntity = GoalEntity(
    id = id,
    userId = userId,
    status = status,
    image = image,
    name = name,
    description = description,
    complexity = complexity,
    createdAt = createdAt,
    completedAt = completedAt
)

fun GoalEntity.toDomain(): Goal = Goal(
    id = id,
    userId = userId,
    status = status,
    image = image,
    name = name,
    description = description,
    complexity = complexity,
    createdAt = createdAt,
    completedAt = completedAt
)


fun Award.toEntity(): AwardEntity = AwardEntity(
    id = id,
    userId = userId,
    status = status,
    image = image,
    name = name,
    description = description,
    price = price,
)

fun AwardEntity.toDomain(): Award = Award(
    id = id,
    userId = userId,
    status = status,
    image = image,
    name = name,
    description = description,
    price = price,
)




















