package com.example.simplyachivs.presentation.goal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplyachivs.R
import com.example.simplyachivs.presentation.components.MainButton
import com.example.simplyachivs.presentation.components.GoalCard
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GoalScreen(onAddNewTarget: () -> Unit, onOpenGoal: (goalId: String) -> Unit) {

    val viewModel: GoalViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.processIntent(GoalIntent.Refresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GoalEffect.NavigateToCreateNewGoal -> onAddNewTarget()
                is GoalEffect.ShowError -> {}
                is GoalEffect.NavigateToGoalDetails -> {
                    onOpenGoal(effect.goalId)
                }
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Мои цели", style = TextStyle(
                fontWeight = FontWeight(900), fontSize = 25.sp
            ), modifier = Modifier
                .padding(10.dp)
                .padding(top = 30.dp)
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.95f),
        ) {
            MainButton(
                when (val uiState = state.value) {
                    is GoalUiState.ShowActiveGoals -> MainBlue
                    is GoalUiState.WithoutActiveGoals -> MainBlue
                    else -> LightGray
                },
                when (val uiState = state.value) {
                    is GoalUiState.ShowActiveGoals -> Color.White
                    is GoalUiState.WithoutActiveGoals -> Color.White
                    else -> Color.Gray
                },
                {
                    viewModel.processIntent(GoalIntent.SelectActiveGoals)
                },
                "Активные",
            )
            Spacer(modifier = Modifier.width(5.dp))

            MainButton(
                when (val uiState = state.value) {
                    is GoalUiState.ShowCompletedGoals -> MainBlue
                    is GoalUiState.WithoutCompletedGoals -> MainBlue
                    else -> LightGray

                },
                when (val uiState = state.value) {
                    is GoalUiState.ShowCompletedGoals -> Color.White
                    is GoalUiState.WithoutCompletedGoals -> Color.White
                    else -> Color.Gray
                },
                {
                    viewModel.processIntent(GoalIntent.SelectCompletedGoals)
                },
                "Завершенные",
            )

        }
        when (val uiState = state.value) {
            GoalUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MainBlue)
                }
            }

            GoalUiState.WithoutActiveGoals -> {
                Card(
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.award_img),
                            contentDescription = "education target",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 10.dp)
                        )
                        Column(modifier = Modifier.padding(bottom = 10.dp)) {
                            Text(
                                text = "Создайте новую цель", style = TextStyle(
                                    fontWeight = FontWeight(600), fontSize = 20.sp
                                ), modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                text = "Поставьте амбициозную задачу!", style = TextStyle(
                                    fontWeight = FontWeight(600),
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                ), modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                            )
                            MainButton(
                                MainBlue,
                                Color.White,
                                { viewModel.processIntent(GoalIntent.AddNewGoal) },
                                "Новая цель",
                                Icons.Default.Add,
                            )
                        }
                    }
                }
            }
            GoalUiState.WithoutCompletedGoals -> Text("Нет завершенных целей")
            is GoalUiState.ShowActiveGoals -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(Color.White),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(10.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(5.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(R.drawable.award_img),
                                    contentDescription = "education target",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(end = 10.dp)
                                )
                                Column(modifier = Modifier.padding(bottom = 10.dp)) {
                                    Text(
                                        text = "Создайте новую цель", style = TextStyle(
                                            fontWeight = FontWeight(600), fontSize = 20.sp
                                        ), modifier = Modifier.padding(top = 10.dp)
                                    )
                                    Text(
                                        text = "Поставьте амбициозную задачу!", style = TextStyle(
                                            fontWeight = FontWeight(600),
                                            fontSize = 15.sp,
                                            color = Color.Gray
                                        ), modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                                    )
                                    MainButton(
                                        MainBlue,
                                        Color.White,
                                        { viewModel.processIntent(GoalIntent.AddNewGoal) },
                                        "Новая цель",
                                        Icons.Default.Add,
                                    )
                                }
                            }
                        }
                    }
                    items(uiState.goals, key = { it.id }) { goal ->
                        GoalCard(
                            goal = goal,
                            onClick = { viewModel.processIntent(GoalIntent.OpenGoalDetails(goal.id.toString())) }
                        )
                    }
                }
            }

            is GoalUiState.ShowCompletedGoals -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(uiState.goals, key = { it.id }) { goal ->
                        GoalCard(
                            goal = goal,
                            onClick = { viewModel.processIntent(GoalIntent.OpenGoalDetails(goal.id.toString())) }
                        )
                    }
                }
            }


        }


    }


}