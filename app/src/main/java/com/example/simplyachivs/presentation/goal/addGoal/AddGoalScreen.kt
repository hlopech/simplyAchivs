package com.example.simplyachivs.presentation.goal.addGoal

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.simplyachivs.presentation.components.StepCard
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.MainBlueLight
import com.example.simplyachivs.ui.theme.Purple40
import kotlinx.coroutines.flow.collectLatest
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddGoalScreen(onBack: () -> Unit) {

    val viewModel: AddGoalViewModel = hiltViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val stepsOffset = 4
    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            val fromIndex = from.index - stepsOffset
            val toIndex = to.index - stepsOffset
            if (fromIndex in state.value.steps.indices && toIndex in state.value.steps.indices) {
                viewModel.moveStep(fromIndex, toIndex)
            }
        }
    )

    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri }
    )

    LaunchedEffect(selectedImageUri) {
        if (selectedImageUri != null) {
            viewModel.processIntent(AddGoalIntent.SelectGoalImage(selectedImageUri))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                AddGoalEffect.LaunchImagePicker -> imagePicker.launch("image/*")
                AddGoalEffect.NavigateToGoals -> onBack()
                AddGoalEffect.SelectComplexity -> {
                    reorderState.listState.animateScrollToItem(state.value.steps.size + stepsOffset)
                }
                is AddGoalEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 50.dp,
                title = { Text("Новая цель", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.processIntent(AddGoalIntent.GoBack) }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = MainBlue,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.processIntent(AddGoalIntent.AddNewGoal) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "",
                            tint = MainBlue,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = 100.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .reorderable(reorderState)
                .detectReorderAfterLongPress(reorderState),
            state = reorderState.listState
        ) {

            // ── Image preview ────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Brush.verticalGradient(listOf(MainBlueLight, Color.White)))
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp))
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.awards_image),
                                contentDescription = "",
                                modifier = Modifier.size(90.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = { viewModel.processIntent(AddGoalIntent.OpenImagePicker) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MainBlue)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.brush_icon),
                            contentDescription = "edit photo",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Name ─────────────────────────────────────────────────────
            item {
                GoalSectionCard {
                    GoalSectionHeader(
                        icon = {
                            Icon(
                                painterResource(R.drawable.star_icon), null,
                                tint = CoinColor, modifier = Modifier.size(22.dp)
                            )
                        },
                        title = "Название цели"
                    )
                    val goalNameError = state.value.goalNameError
                    OutlinedTextField(
                        value = state.value.goalName,
                        onValueChange = { viewModel.processIntent(AddGoalIntent.ChangeGoalName(it)) },
                        placeholder = { Text("Введите название...", color = Color.Gray) },
                        isError = goalNameError != null,
                        supportingText = {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (goalNameError != null) {
                                    Text(goalNameError, color = Color.Red, fontSize = 12.sp)
                                } else {
                                    Spacer(Modifier.weight(1f))
                                }
                                Text(
                                    "${state.value.goalName.length}/50",
                                    color = if (state.value.goalName.length >= 45) Color.Red else Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = LightGray,
                            focusedIndicatorColor = MainBlue,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            errorIndicatorColor = Color.Red,
                            errorContainerColor = Color.White
                        )
                    )
                }
            }

            // ── Description ──────────────────────────────────────────────
            item {
                GoalSectionCard {
                    GoalSectionHeader(
                        icon = {
                            Icon(
                                painterResource(R.drawable.draw_icon), null,
                                tint = DarkGreen, modifier = Modifier.size(22.dp)
                            )
                        },
                        title = "Описание"
                    )
                    OutlinedTextField(
                        value = state.value.goalDescription,
                        onValueChange = { viewModel.processIntent(AddGoalIntent.ChangeGoalDescription(it)) },
                        placeholder = { Text("Добавьте описание (необязательно)", color = Color.Gray) },
                        supportingText = {
                            Text(
                                "${state.value.goalDescription.length}/200",
                                color = if (state.value.goalDescription.length >= 180) Color.Red else Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = LightGray,
                            focusedIndicatorColor = MainBlue,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                }
            }

            // ── Steps input ──────────────────────────────────────────────
            item {
                GoalSectionCard {
                    GoalSectionHeader(
                        icon = {
                            Icon(
                                painterResource(R.drawable.flag_icon), null,
                                tint = Purple40, modifier = Modifier.size(22.dp)
                            )
                        },
                        title = "Промежуточные шаги"
                    )
                    val stepError = state.value.newStepNameError
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .border(
                                1.dp,
                                if (stepError != null) Color.Red else LightGray,
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            "${state.value.steps.size + 1}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MainBlue,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                        VerticalDivider(modifier = Modifier.height(56.dp))
                        TextField(
                            value = state.value.newStepName,
                            onValueChange = { viewModel.processIntent(AddGoalIntent.ChangeNewStepName(it)) },
                            placeholder = { Text("Введите шаг...", color = Color.Gray) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    viewModel.processIntent(AddGoalIntent.AddNewStep(state.value.newStepName))
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "add step",
                                        tint = MainBlue,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            },
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            )
                        )
                    }
                    if (stepError != null) {
                        Text(
                            text = stepError,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }
            }

            // ── Steps list (reorderable) ──────────────────────────────────
            items(state.value.steps, key = { it.id }) { step ->
                ReorderableItem(
                    reorderState,
                    modifier = Modifier.height(70.dp),
                    key = step.id
                ) { isDragging ->
                    StepCard(
                        step = step,
                        { viewModel.processIntent(AddGoalIntent.DeleteStep(step.id, step.position)) },
                        modifier = Modifier
                            .shadow(if (isDragging) 8.dp else 2.dp)
                            .animateItemPlacement()
                    )
                }
            }

            // ── Complexity ───────────────────────────────────────────────
            item {
                GoalSectionCard {
                    GoalSectionHeader(
                        icon = {
                            Icon(
                                painterResource(R.drawable.select_complexity_icon), null,
                                tint = MainBlue, modifier = Modifier.size(22.dp)
                            )
                        },
                        title = "Сложность"
                    )
                    Text(
                        "От сложности зависит награда за выполнение цели",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ComplexityCard(
                            label = "Легкая",
                            color = DarkGreen,
                            isSelected = state.value.complexity == GoalComplexity.EASY,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.processIntent(AddGoalIntent.SelectGoalComplexity(GoalComplexity.EASY)) }
                        )
                        ComplexityCard(
                            label = "Средняя",
                            color = CoinColor,
                            isSelected = state.value.complexity == GoalComplexity.MEDIUM,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.processIntent(AddGoalIntent.SelectGoalComplexity(GoalComplexity.MEDIUM)) }
                        )
                        ComplexityCard(
                            label = "Сложная",
                            color = Color.Red,
                            isSelected = state.value.complexity == GoalComplexity.HARD,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.processIntent(AddGoalIntent.SelectGoalComplexity(GoalComplexity.HARD)) }
                        )
                    }
                }
            }

            // ── Reward (shown when complexity is selected) ────────────────
            item {
                val complexity = state.value.complexity
                if (complexity != null) {
                    GoalSectionCard {
                        GoalSectionHeader(
                            icon = {
                                Image(
                                    painterResource(R.drawable.award_img), null,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            title = "Награда за цель"
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEEF6FF))
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.xp_img),
                                    contentDescription = "xp",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "${complexity.xp} XP",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MainBlue
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CoinColor.copy(alpha = 0.12f))
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.coin_img),
                                    contentDescription = "coins",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "${complexity.coins}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB8860B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComplexityCard(
    label: String,
    color: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(2.dp, color) else BorderStroke(1.dp, LightGray),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.08f) else Color(0xFFFAFAFA)
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.complexity_icon),
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(if (isSelected) 44.dp else 36.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = color,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GoalSectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun GoalSectionHeader(icon: @Composable () -> Unit, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(Modifier.width(8.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
    Spacer(Modifier.height(8.dp))
}
