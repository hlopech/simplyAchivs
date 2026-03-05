package com.example.simplyachivs.data.repositoryImpl

import com.example.simplyachivs.data.dao.EventDao
import com.example.simplyachivs.data.entity.EventEntity
import com.example.simplyachivs.domain.model.event.Event
import com.example.simplyachivs.domain.model.event.EventType
import com.example.simplyachivs.domain.repository.EventRepository
import java.util.UUID
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventDao: EventDao) : EventRepository {

    override suspend fun track(event: Event) {
        eventDao.insert(event.toEntity())
    }

    override suspend fun getAllByUser(userId: UUID): List<Event> {
        return eventDao.getAllByUser(userId).map { it.toDomain() }
    }

    override suspend fun countByType(userId: UUID, type: EventType): Int {
        return eventDao.countByType(userId, type)
    }

    private fun Event.toEntity() = EventEntity(
        id = id,
        userId = userId,
        type = type,
        relatedEntityId = relatedEntityId,
        relatedEntityType = relatedEntityType,
        metadata = metadata,
        createdAt = createdAt,
    )

    private fun EventEntity.toDomain() = Event(
        id = id,
        userId = userId,
        type = type,
        relatedEntityId = relatedEntityId,
        relatedEntityType = relatedEntityType,
        metadata = metadata,
        createdAt = createdAt,
    )
}
