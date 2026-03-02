package com.example.simplyachivs.presentation.shop.addAward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.usecase.award.AddAwardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAwardViewModel @Inject constructor(
    private val addAwardUseCase: AddAwardUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AddAwardUiState>(AddAwardUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddAwardEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()


    private fun sendEffect(effect: AddAwardEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    fun processIntent(intent: AddAwardIntent) {
        when (intent) {
            AddAwardIntent.GoBack -> sendEffect(AddAwardEffect.NavigateToAwards)
            AddAwardIntent.AddNewAward -> {
                val name = _state.value.awardName.trim()
                val nameError = when {
                    name.isBlank() -> "Введите название награды"
                    name.length < 3 -> "Название должно быть не менее 3 символов"
                    else -> null
                }
                val priceError = if (_state.value.price < 1) "Укажите стоимость (минимум 1)" else null

                if (nameError != null || priceError != null) {
                    _state.update { it.copy(awardNameError = nameError, priceError = priceError) }
                    return
                }
                _state.update { it.copy(awardNameError = null, priceError = null) }
                sendEffect(AddAwardEffect.NavigateToAwards)
            }

            is AddAwardIntent.ChangeAwardName -> _state.update {
                it.copy(awardName = intent.name.take(50), awardNameError = null)
            }
            is AddAwardIntent.ChangeAwardDescription -> _state.update {
                it.copy(awardDescription = intent.description.take(200))
            }
            is AddAwardIntent.ChangeAwardPrice -> _state.update {
                it.copy(price = intent.price, priceError = null)
            }
            is AddAwardIntent.ChangeAwardPriceSlider -> _state.update {
                it.copy(price = intent.price, priceError = null)
            }
            is AddAwardIntent.SelectGoalImage -> TODO()
        }

    }

}