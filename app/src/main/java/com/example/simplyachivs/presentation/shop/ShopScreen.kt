package com.example.simplyachivs.presentation.shop

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplyachivs.R
import com.example.simplyachivs.presentation.components.AwardCard
import com.example.simplyachivs.presentation.components.AwardDetailsDialog
import com.example.simplyachivs.ui.theme.MainBlue
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(onAddAward: () -> Unit) {

    val viewModel: ShopViewModel = hiltViewModel()

    val state = viewModel.state.collectAsStateWithLifecycle()

    var showDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ShopEffect.HideAwardDetailsDialog -> showDialog = false
                ShopEffect.NavigateToCreateNewAward -> onAddAward()
                is ShopEffect.ShowAwardDetailsDialog -> showDialog = true
                is ShopEffect.ShowError -> TODO()
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.processIntent(ShopIntent.AddNewAward) },
                modifier = Modifier
                    .padding(bottom = 60.dp),
                elevation = FloatingActionButtonDefaults.elevation(15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = MainBlue,
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(expandedHeight = 50.dp, title = {
                Text("Магазин наград")
            }, navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.shop_icon),
                        contentDescription = "",
                        tint = MainBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }, actions = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    state.value.progress?.let {
                        Image(
                            painter = painterResource(R.drawable.coin_img),
                            contentDescription = "coins",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            state.value.progress?.coin.toString(),
                            modifier = Modifier.padding(5.dp),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 23.sp,
                                fontWeight = FontWeight(600),
                                textAlign = TextAlign.Center
                            )
                        )

                    }

                }
            })
        }) { paddingValues ->

        if (showDialog) {
            AwardDetailsDialog(
                state.value.selectedAward,
                onDismiss = { viewModel.processIntent(ShopIntent.HideAwardDetails) },
                onConfirm = { viewModel.processIntent(ShopIntent.BuyAward(state.value.selectedAward!!)) })
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            if (state.value.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MainBlue)
                }
            } else
                Box(modifier = Modifier.padding(bottom = 100.dp)) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                    ) {
                        items(state.value.awards) { award ->
                            AwardCard(
                                award,
                                { viewModel.processIntent(ShopIntent.OpenAwardDetails(award)) }
                            )
                        }
                    }

                }

        }

    }

}