package com.example.simplyachivs.presentation.home

sealed interface HomeEffect {
    data class ShowError(val message: String) : HomeEffect
    object NavigateToProfile : HomeEffect
}