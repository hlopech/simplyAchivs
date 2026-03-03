package com.example.simplyachivs.presentation.splash

sealed interface SplashEffect {
    object NavigateToHome : SplashEffect
    object NavigateToLogin : SplashEffect
}
