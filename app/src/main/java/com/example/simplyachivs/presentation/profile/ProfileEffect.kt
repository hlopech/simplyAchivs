package com.example.simplyachivs.presentation.profile

sealed interface ProfileEffect {
    object GoBack : ProfileEffect
    data class ShowError(val message: String) : ProfileEffect
    data class NavigateToOption(val optionId: Int) : ProfileEffect
    object ShowDialog: ProfileEffect
    object HideDialog: ProfileEffect
    object NavigateToSettings : ProfileEffect
}