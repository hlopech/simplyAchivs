package com.example.simplyachivs.domain.model.award

import java.util.UUID

data class Award(
    val id: UUID,
    val userId: UUID,
    val status:AwardStatus,
    val image: Int,
    val name:String,
    val description:String,
    val price: Int,
    val createdAt: Long,
    val completedAt: Long?=null
    )
