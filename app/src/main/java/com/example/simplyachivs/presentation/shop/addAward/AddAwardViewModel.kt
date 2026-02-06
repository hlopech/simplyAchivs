package com.example.simplyachivs.presentation.shop.addAward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddAwardViewModel : ViewModel() {

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

                //logic addition award

                sendEffect(AddAwardEffect.NavigateToAwards)

            }

            is AddAwardIntent.ChangeAwardName -> _state.update { it.copy(awardName = intent.name) }
            is AddAwardIntent.ChangeAwardDescription -> _state.update { it.copy(awardDescription = intent.description) }
            is AddAwardIntent.ChangeAwardPrice -> _state.update { it.copy(price = intent.price) }
            is AddAwardIntent.ChangeAwardPriceSlider -> _state.update { it.copy(price = intent.price) }
            is AddAwardIntent.SelectGoalImage -> TODO()
        }

    }

}