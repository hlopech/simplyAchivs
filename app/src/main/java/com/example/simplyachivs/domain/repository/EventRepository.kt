package com.example.simplyachivs.domain.repository

import com.example.simplyachivs.domain.model.event.Event
import java.util.UUID

interface EventRepository {
    suspend fun createEvent(event: Event)
    suspend fun getEvents(userId: UUID): List<Event>
    suspend fun updateEvent(event: Event)
    suspend fun getEvent(eventId: UUID): Event
}