package com.example.simplyachivs.presentation.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.event.Event
import com.example.simplyachivs.domain.model.event.EventType
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.service.AchievementNotifier
import com.example.simplyachivs.domain.usecase.award.DeleteAwardUseCase
import com.example.simplyachivs.domain.usecase.event.TrackEventUseCase
import com.example.simplyachivs.domain.usecase.award.GetActiveAwardsUseCase
import com.example.simplyachivs.domain.usecase.award.GetAllAwardsUseCase
import com.example.simplyachivs.domain.usecase.progress.CompleteAwardUseCase
import com.example.simplyachivs.domain.usecase.progress.GetUserProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val getAllAwardsUseCase: GetAllAwardsUseCase,
    private val getActiveAwardsUseCase: GetActiveAwardsUseCase,
    private val getUserProgressUseCase: GetUserProgressUseCase,
    private val completeAwardsUseCase: CompleteAwardUseCase,
    private val deleteAwardUseCase: DeleteAwardUseCase,
    private val sessionRepository: SessionRepository,
    private val trackEventUseCase: TrackEventUseCase,
    private val achievementNotifier: AchievementNotifier,
) : ViewModel() {

    private val _state = MutableStateFlow<ShopUiState>(ShopUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ShopEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    private lateinit var userId: UUID
    private fun sendEffect(effect: ShopEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    init {
        loadActiveAwards()
    }

    private fun loadProgress() {
        viewModelScope.launch {
            getUserProgressUseCase(userId).onSuccess { progress ->
                _state.update {
                    it.copy(
                        progress = progress
                    )
                }
            }

        }
    }

    private fun loadActiveAwards() {
        viewModelScope.launch {
            userId = sessionRepository.userId.first() ?: return@launch
            getActiveAwardsUseCase(userId = userId).onSuccess { result ->
                _state.update { it.copy(awards = result) }
            }
            loadProgress()
        }
    }

    fun processIntent(intent: ShopIntent) {
        when (intent) {
            ShopIntent.AddNewAward -> sendEffect(ShopEffect.NavigateToCreateNewAward)

            ShopIntent.HideAwardDetails -> {
                sendEffect(ShopEffect.HideAwardDetailsDialog)
                _state.update { it.copy(selectedAward = null) }
            }

            is ShopIntent.OpenAwardDetails -> {
                _state.update { it.copy(selectedAward = intent.award) }
                sendEffect(ShopEffect.ShowAwardDetailsDialog(intent.award))
            }

            is ShopIntent.DeleteAward -> {
                viewModelScope.launch {
                    deleteAwardUseCase(intent.award).onSuccess {
                        loadActiveAwards()
                        sendEffect(ShopEffect.HideAwardDetailsDialog)
                    }.onFailure {
                        sendEffect(ShopEffect.ShowError("Не удалось удалить награду"))
                    }
                }
            }

            is ShopIntent.BuyAward -> {
                viewModelScope.launch {
                    completeAwardsUseCase(
                        userId,
                        state.value.progress!!,
                        intent.award.price,
                        intent.award
                    ).onSuccess {
                        trackEventUseCase(Event.now(
                            userId = userId,
                            type = EventType.AWARD_PURCHASED,
                            relatedEntityId = intent.award.id,
                            relatedEntityType = "AWARD",
                        ))
                        achievementNotifier.checkForNewAchievements(userId)
                        loadActiveAwards()
                        sendEffect(ShopEffect.HideAwardDetailsDialog)
                    }.onFailure {
                        sendEffect(ShopEffect.HideAwardDetailsDialog)
                        sendEffect(ShopEffect.ShowError(it.message.toString()))
                    }
                }
            }
        }
    }
}
