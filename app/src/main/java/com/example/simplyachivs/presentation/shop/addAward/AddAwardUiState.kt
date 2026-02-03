package com.example.simplyachivs.presentation.shop.addAward

import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import com.example.simplyachivs.domain.model.goal.Step

data class AddAwardUiState(
    val isLoading: Boolean = false,
    val awardName: String = "",
    val awardDescription: String = "",
    val error: AddAwardError? = null,
    val price: Int = 0,
    val goalImage: Int? = null,

    )
