package com.example.simplyachivs.presentation.profile

import com.example.simplyachivs.domain.model.user.User
import com.example.simplyachivs.domain.model.user.UserProgress

data class ProfileUiState(
    val user: User? = null,
    val loading: Boolean = false,
    val progress: UserProgress?=null,
    val error: String? = null


)
