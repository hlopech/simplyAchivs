package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: UUID,
    val name: String,
    val avatar: String,
    val createdAt: Instant,
    val lastActiveAt: Instant
)
