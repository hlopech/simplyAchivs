package com.example.simplyachivs.presentation.shop

import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.model.user.UserProgress

data class ShopUiState(
    val isLoading: Boolean = false,
    val awards: List<Award> = emptyList(),
    val progress: UserProgress? = null,
    val selectedAward: Award? = null

)
