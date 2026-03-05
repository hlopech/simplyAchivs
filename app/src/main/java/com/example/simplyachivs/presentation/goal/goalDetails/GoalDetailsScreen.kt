package com.example.simplyachivs.presentation.goal.goalDetails

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.simplyachivs.R
import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.model.goal.GoalStatus
import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.model.goal.StepStatus
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.MainBlueLight
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetails(goalId: String, onBack: () -> Unit) {

    val viewModel: GoalDetailsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(goalId) { viewModel.load(goalId) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                GoalDetailsEffect.NavigateBack -> onBack()
                GoalDetailsEffect.GoalDeleted -> onBack()
                is GoalDetailsEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is GoalDetailsEffect.GoalCompleted -> snackbarHostState.showSnackbar(
                    "+${effect.xp} XP и +${effect.coins} монет получено!"
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { viewModel.processIntent(GoalDetailsIntent.GoBack) }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Назад",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
                actions = {
                    if (state is GoalDetailsUiState.Content) {
                        IconButton(onClick = { viewModel.processIntent(GoalDetailsIntent.RequestDeleteGoal) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Удалить цель",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
        containerColor = Color.White,
    ) { padding ->
        when (val s = state) {
            GoalDetailsUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MainBlue)
                }
            }

            is GoalDetailsUiState.Error -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(s.message, color = Color.Gray, textAlign = TextAlign.Center)
                }
            }

            is GoalDetailsUiState.Content -> {
                GoalDetailsContent(
                    state = s,
                    paddingValues = padding,
                    onIntent = viewModel::processIntent,
                )
                if (s.showCompleteDialog) {
                    CompleteGoalDialog(
                        allStepsDone = s.allStepsDone,
                        xp = s.goal.complexity.xp,
                        coins = s.goal.complexity.coins,
                        onConfirm = { viewModel.processIntent(GoalDetailsIntent.ConfirmCompleteGoal) },
                        onDismiss = { viewModel.processIntent(GoalDetailsIntent.DismissCompleteDialog) },
                    )
                }
                if (s.showDeleteDialog) {
                    DeleteGoalDialog(
                        goalName = s.goal.name,
                        onConfirm = { viewModel.processIntent(GoalDetailsIntent.ConfirmDeleteGoal) },
                        onDismiss = { viewModel.processIntent(GoalDetailsIntent.DismissDeleteDialog) },
                    )
                }
            }
        }
    }
}

@Composable
private fun GoalDetailsContent(
    state: GoalDetailsUiState.Content,
    paddingValues: PaddingValues,
    onIntent: (GoalDetailsIntent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        // Hero image
        item { GoalHeroImage(goal = state.goal, topPadding = paddingValues.calculateTopPadding()) }

        // Info card
        item { GoalInfoCard(state = state) }

        // Steps header
        if (state.steps.isNotEmpty()) {
            item {
                Text(
                    text = "Шаги",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        // Steps
        items(state.steps, key = { it.id }) { step ->
            StepCheckCard(
                step = step,
                enabled = state.goal.status == GoalStatus.ACTIVE && !state.isCompletingGoal,
                onToggle = { onIntent(GoalDetailsIntent.ToggleStep(step)) }
            )
        }

        // Complete button
        if (state.goal.status == GoalStatus.ACTIVE) {
            item {
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onIntent(GoalDetailsIntent.RequestCompleteGoal) },
                    enabled = state.allStepsDone && !state.isCompletingGoal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen,
                        disabledContainerColor = LightGray
                    )
                ) {
                    if (state.isCompletingGoal) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (state.allStepsDone) "Завершить цель" else "Выполните все шаги",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalHeroImage(goal: Goal, topPadding: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp + topPadding)
    ) {
        if (goal.image != null) {
            Image(
                painter = rememberAsyncImagePainter(goal.image),
                contentDescription = goal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(MainBlue, MainBlueLight)))
            )
            Image(
                painter = painterResource(R.drawable.education_target_img),
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun GoalInfoCard(state: GoalDetailsUiState.Content) {
    val complexityColor = when (state.goal.complexity) {
        GoalComplexity.EASY -> DarkGreen
        GoalComplexity.MEDIUM -> CoinColor
        GoalComplexity.HARD -> Color.Red
    }
    val complexityLabel = when (state.goal.complexity) {
        GoalComplexity.EASY -> "Лёгкая"
        GoalComplexity.MEDIUM -> "Средняя"
        GoalComplexity.HARD -> "Сложная"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Status badge (if completed)
            if (state.goal.status == GoalStatus.COMPLETED) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkGreen.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Завершена", color = DarkGreen, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(10.dp))
            }

            // Name
            Text(
                text = state.goal.name,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )

            // Description
            if (state.goal.description.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = state.goal.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            // Progress bar
            if (state.steps.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Прогресс",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "${state.completedCount} / ${state.totalCount}",
                        fontSize = 13.sp,
                        color = MainBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = if (state.allStepsDone) DarkGreen else MainBlue,
                    trackColor = LightGray,
                    gapSize = 0.dp,
                    drawStopIndicator = {},
                    strokeCap = StrokeCap.Round
                )
                Spacer(Modifier.height(14.dp))
            }

            // Complexity + Reward row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Complexity
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(complexityColor.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.complexity_icon),
                        contentDescription = null,
                        tint = complexityColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(complexityLabel, color = complexityColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                // XP
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MainBlue.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.xp_img),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("+${state.goal.complexity.xp} XP", color = MainBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                // Coins
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(CoinColor.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.coin_img),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("+${state.goal.complexity.coins}", color = CoinColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun StepCheckCard(step: Step, enabled: Boolean, onToggle: () -> Unit) {
    val isDone = step.status == StepStatus.COMPLETED
    val bgColor by animateColorAsState(
        targetValue = if (isDone) DarkGreen.copy(alpha = 0.07f) else Color.White,
        animationSpec = tween(300),
        label = "stepBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isDone) DarkGreen.copy(alpha = 0.4f) else LightGray,
        animationSpec = tween(300),
        label = "stepBorder"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .then(
                Modifier.padding(1.dp)
            )
    ) {
        IconButton(
            onClick = onToggle,
            enabled = enabled,
            modifier = Modifier.size(48.dp)
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Выполнено",
                    tint = DarkGreen,
                    modifier = Modifier.size(26.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .then(
                            Modifier.padding(1.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(borderColor)
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }
        }

        Text(
            text = "${step.position}. ${step.name}",
            fontSize = 15.sp,
            color = if (isDone) Color.Gray else Color.Black,
            fontWeight = if (isDone) FontWeight.Normal else FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            maxLines = 2,
        )
    }
}

@Composable
private fun CompleteGoalDialog(
    allStepsDone: Boolean,
    xp: Int,
    coins: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Завершить цель?", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!allStepsDone) {
                    Text(
                        "Не все шаги выполнены. Вы уверены, что хотите завершить цель?",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                } else {
                    Text(
                        "Отличная работа! Вы получите:",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.xp_img),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("+$xp XP", fontWeight = FontWeight.Bold, color = MainBlue)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.coin_img),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("+$coins", fontWeight = FontWeight.Bold, color = CoinColor)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Завершить", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun DeleteGoalDialog(
    goalName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удалить цель?", fontWeight = FontWeight.Bold) },
        text = {
            Text(
                "«$goalName» будет удалена безвозвратно вместе со всеми шагами.",
                color = Color.Gray,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Удалить", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
