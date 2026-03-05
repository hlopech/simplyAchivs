package com.example.simplyachivs.domain.repository

import com.example.simplyachivs.domain.model.event.Event
import com.example.simplyachivs.domain.model.event.EventType
import java.util.UUID

interface EventRepository {
    suspend fun track(event: Event)
    suspend fun getAllByUser(userId: UUID): List<Event>
    suspend fun countByType(userId: UUID, type: EventType): Int
}
