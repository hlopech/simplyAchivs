package com.example.simplyachivs.presentation.shop

import java.util.UUID

sealed interface ShopIntent {
    object AddNewAward : ShopIntent
    data class OpenAwardDetails(val awardId: UUID): ShopIntent
}