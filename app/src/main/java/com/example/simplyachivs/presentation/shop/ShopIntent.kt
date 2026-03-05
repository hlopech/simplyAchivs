package com.example.simplyachivs.presentation.shop

import com.example.simplyachivs.domain.model.award.Award
import java.util.UUID

sealed interface ShopIntent {
    object AddNewAward : ShopIntent
    object HideAwardDetails : ShopIntent

    data class BuyAward(val award: Award) : ShopIntent
    data class OpenAwardDetails(val award: Award) : ShopIntent
    data class DeleteAward(val award: Award) : ShopIntent

}