package com.example.simplyachivs.presentation.shop.addAward

sealed interface AddAwardEffect {
    data class ShowError(val message: String) : AddAwardEffect
    object NavigateToAwards: AddAwardEffect

}