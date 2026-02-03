package com.example.simplyachivs.presentation.shop.addAward


sealed interface AddAwardError {
    object EmptyName : AddAwardError
    data class General(val message: String) : AddAwardError
}