package com.example.simplyachivs.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplyachivs.R
import com.example.simplyachivs.presentation.components.EditUserProfile
import com.example.simplyachivs.presentation.components.ProfileInfoSection
import com.example.simplyachivs.presentation.components.ProfileOptions
import com.example.simplyachivs.ui.theme.MainBlue
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit, onOpenSettings: () -> Unit) {

    val viewModel: ProfileViewModel = hiltViewModel()

    val state = viewModel.state.collectAsStateWithLifecycle()

    var showDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ProfileEffect.GoBack -> onBack()
                ProfileEffect.HideDialog -> showDialog = false
                is ProfileEffect.NavigateToOption -> TODO()
                is ProfileEffect.ShowError -> TODO()
                is ProfileEffect.ShowDialog -> showDialog = true
                ProfileEffect.NavigateToSettings -> onOpenSettings()

            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 50.dp, title = {
                    Text("Профиль")
                }, navigationIcon = {
                    IconButton(onClick = { viewModel.processIntent(ProfileIntent.GoBack) }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = MainBlue,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.processIntent(ProfileIntent.OpenEditDialog) }) {
                        Icon(
                            painter = painterResource(R.drawable.draw_icon),
                            contentDescription = "",
                            tint = MainBlue,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

            )
        }) { paddingValues ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = paddingValues.calculateTopPadding(), bottom = 100.dp)
        ) {
            if (showDialog) {
                EditUserProfile(
                    currentName = "Никита",
                    onDismiss = { viewModel.processIntent(ProfileIntent.CloseEditDialog) },
                    onConfirm = { newName, newPhoto ->
                        viewModel.processIntent(ProfileIntent.ConfirmEdition(newName, newPhoto))
                    }
                )
            }
            ProfileInfoSection()
            Spacer(Modifier.height(0.dp))
            ProfileOptions(
                onSettingsClick = { viewModel.processIntent(ProfileIntent.OpenSettings) }
            )
        }

    }
}