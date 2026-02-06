package com.example.simplyachivs.presentation.shop

import com.example.simplyachivs.domain.model.award.Award

sealed interface ShopEffect {
    data class ShowError(val message: String) : ShopEffect
    data class ShowAwardDetailsDialog(val award: Award) : ShopEffect

    object HideAwardDetailsDialog : ShopEffect
    object NavigateToCreateNewAward : ShopEffect

}