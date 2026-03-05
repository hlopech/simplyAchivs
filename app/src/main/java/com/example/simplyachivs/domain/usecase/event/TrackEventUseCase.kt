package com.example.simplyachivs.domain.usecase.event

import com.example.simplyachivs.domain.model.event.Event
import com.example.simplyachivs.domain.repository.EventRepository
import javax.inject.Inject

class TrackEventUseCase @Inject constructor(private val eventRepository: EventRepository) {
    suspend operator fun invoke(event: Event) = runCatching { eventRepository.track(event) }
}
