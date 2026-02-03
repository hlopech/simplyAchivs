package com.example.simplyachivs.presentation.shop

import java.util.UUID

sealed interface ShopEffect {
    data class ShowError(val message: String) : ShopEffect
    data class NavigateToAwardDetails(val awardId: UUID) : ShopEffect

}