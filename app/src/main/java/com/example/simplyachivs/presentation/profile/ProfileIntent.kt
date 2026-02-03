package com.example.simplyachivs.presentation.profile

sealed interface ProfileIntent {
    object GoBack : ProfileIntent
    data class SelectOption(val optionId: Int) : ProfileIntent
}