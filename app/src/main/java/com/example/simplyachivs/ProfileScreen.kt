package com.example.simplyachivs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplyachivs.components.ProfileInfoSection
import com.example.simplyachivs.components.ProfileOptions
import com.example.simplyachivs.ui.theme.MainBlue

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 50.dp, title = {
                    Text("Профиль")
                }, navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = MainBlue,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
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
                .fillMaxWidth().fillMaxHeight()
                .padding(top = paddingValues.calculateTopPadding(), bottom = 100.dp)
//                .verticalScroll(rememberScrollState(), true)
        ) {
            ProfileInfoSection()
            Spacer(Modifier.height(0.dp))
            ProfileOptions(listOf(1, 2, 3, 4))
        }

    }
}