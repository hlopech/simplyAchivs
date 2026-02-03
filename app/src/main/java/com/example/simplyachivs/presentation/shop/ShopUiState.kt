package com.example.simplyachivs.presentation.shop

import com.example.simplyachivs.domain.model.award.Award

data class ShopUiState(

    val awards: List<Award> = emptyList(),
    val coins: Int = 0

)
