package com.example.simplyachivs.presentation.shop.addAward

import com.example.simplyachivs.domain.model.complexity.GoalComplexity

sealed interface AddAwardIntent {

    object AddNewAward : AddAwardIntent
    object GoBack : AddAwardIntent

    data class ChangeAwardName(val name: String) :AddAwardIntent
    data class ChangeAwardDescription(val description: String) : AddAwardIntent
    data class SelectGoalImage(val image: Int) : AddAwardIntent
    data class ChangeAwardPrice(val price: Int) : AddAwardIntent
    data class ChangeAwardPriceSlider(val price: Int) : AddAwardIntent



}