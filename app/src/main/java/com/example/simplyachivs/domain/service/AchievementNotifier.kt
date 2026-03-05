package com.example.simplyachivs.domain.service

import com.example.simplyachivs.domain.model.achievement.Achievement
import com.example.simplyachivs.domain.model.achievement.AchievementStatus
import com.example.simplyachivs.domain.usecase.achievement.GetAchievementsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementNotifier @Inject constructor(
    private val getAchievementsUseCase: GetAchievementsUseCase,
) {
    private val _events = MutableSharedFlow<Achievement>(extraBufferCapacity = 5)
    val events: SharedFlow<Achievement> = _events.asSharedFlow()

    // IDs already known to be completed — populated on first check so we don't spam on app launch
    private val seenCompleted = mutableSetOf<String>()
    private var initialized = false
    private val mutex = Mutex()

    suspend fun checkForNewAchievements(userId: UUID) = mutex.withLock {
        getAchievementsUseCase(userId).onSuccess { achievements ->
            val completedNow = achievements
                .filter { it.status == AchievementStatus.COMPLETED }

            if (!initialized) {
                seenCompleted += completedNow.map { it.id }
                initialized = true
                return@onSuccess
            }

            completedNow
                .filter { it.id !in seenCompleted }
                .forEach { achievement ->
                    seenCompleted += achievement.id
                    _events.emit(achievement)
                }
        }
    }
}
