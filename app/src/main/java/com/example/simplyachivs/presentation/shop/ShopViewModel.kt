package com.example.simplyachivs.presentation.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.usecase.award.GetActiveAwardsUseCase
import com.example.simplyachivs.domain.usecase.award.GetAllAwardsUseCase
import com.example.simplyachivs.presentation.shop.ShopEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val getAllAwardsUseCase: GetAllAwardsUseCase,
    private val getActiveAwardsUseCase: GetActiveAwardsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ShopUiState>(ShopUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ShopEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    private fun sendEffect(effect: ShopEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
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

            is ShopIntent.BuyAward -> TODO()
        }

    }

}
