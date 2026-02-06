package com.example.simplyachivs.presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplyachivs.R
import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import com.example.simplyachivs.presentation.components.MainButton
import com.example.simplyachivs.presentation.goal.GoalIntent

import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.LightGreen
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.Orange
import com.example.simplyachivs.ui.theme.Pink40
import com.example.simplyachivs.ui.theme.Pink80
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onOpenProfile: () -> Unit) {

    val viewModel: HomeViewModel = viewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()

    val hasNewMessage = remember { mutableStateOf(true) }
    var isAddSheetVisible by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val targetProgress = remember(state.value.tasks) {
        val completed = state.value.tasks?.count { it.completedAt != null } ?: 0
        val total = state.value.tasks?.size ?: 1
        completed.toFloat() / total
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 500) // можешь настроить скорость
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                HomeEffect.HideAddTaskSheet -> isAddSheetVisible = false
                HomeEffect.NavigateToProfile -> {
                    onOpenProfile()
                }

                HomeEffect.ShowAddTaskSheet -> isAddSheetVisible = true
                is HomeEffect.ShowError -> TODO()
            }
        }
    }

    if (isAddSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.processIntent(HomeIntent.HideAddTaskSheet) },
            sheetState = bottomSheetState
        ) {

            Column(
                Modifier.fillMaxWidth(0.95f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(top = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.star_icon),
                            contentDescription = "target name input",
                            tint = CoinColor,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            "Название задачи", style = TextStyle(
                                fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Black
                            )
                        )
                    }
                    OutlinedTextField(
                        value = "${state.value.newTask?.name}",
                        onValueChange = { viewModel.processIntent(HomeIntent.ChangeTaskName(it)) },
                        placeholder = {
                            Text(
                                "Введите название...", style = TextStyle(
                                    fontWeight = FontWeight(400),
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                            )
                        },
                        modifier = Modifier
                            .height(70.dp)
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = LightGray,
                            focusedIndicatorColor = MainBlue,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(top = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.select_complexity_icon),
                        contentDescription = "awards input",
                        tint = MainBlue,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        "Сложность", style = TextStyle(
                            fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Black
                        )
                    )
                }

                Text(
                    text = "В зависимости от выбранной сложности, при выполнении задачи полностью, будет начисленна награда, чем вышел сложность, тем выше награда.",
                    style = TextStyle(
                        fontWeight = FontWeight(400),
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(10.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround

                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        when (state.value.newTask?.complexity) {
                            TaskComplexity.EASY -> {
                                IconButton(
                                    onClick = {
                                        viewModel.processIntent(
                                            HomeIntent.SelectTaskComplexity(
                                                TaskComplexity.EASY
                                            )
                                        )
                                    }, modifier = Modifier.size(60.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.complexity_icon),
                                        contentDescription = "",
                                        tint = DarkGreen,
                                        modifier = Modifier.size(100.dp)
                                    )
                                }
                                Text(
                                    text = "Легкая", style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 18.sp,
                                        color = DarkGreen
                                    )
                                )

                            }

                            else -> {
                                IconButton(onClick = {
                                    viewModel.processIntent(
                                        HomeIntent.SelectTaskComplexity(
                                            TaskComplexity.EASY
                                        )
                                    )
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.complexity_icon),
                                        contentDescription = "",
                                        tint = DarkGreen,
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                                Text(
                                    text = "Легкая", style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 16.sp,
                                        color = DarkGreen
                                    )
                                )
                            }
                        }


                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        when (state.value.newTask?.complexity) {
                            TaskComplexity.MEDIUM -> {
                                IconButton(
                                    onClick = {
                                        viewModel.processIntent(
                                            HomeIntent.SelectTaskComplexity(
                                                TaskComplexity.MEDIUM
                                            )
                                        )
                                    },
                                    modifier = Modifier.size(60.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.complexity_icon),
                                        contentDescription = "",
                                        tint = CoinColor,
                                        modifier = Modifier.size(100.dp)
                                    )
                                }
                                Text(
                                    text = "Средняя", style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 18.sp,
                                        color = CoinColor
                                    )
                                )
                            }

                            else -> {
                                IconButton(
                                    onClick = {
                                        viewModel.processIntent(
                                            HomeIntent.SelectTaskComplexity(
                                                TaskComplexity.MEDIUM
                                            )
                                        )
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.complexity_icon),
                                        contentDescription = "",
                                        tint = CoinColor,
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                                Text(
                                    text = "Средняя", style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 16.sp,
                                        color = CoinColor
                                    )
                                )
                            }
                        }


                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        when (state.value.newTask?.complexity) {
                            TaskComplexity.HARD -> {
                                IconButton(onClick = {
                                    viewModel.processIntent(
                                        HomeIntent.SelectTaskComplexity(
                                            TaskComplexity.HARD
                                        )
                                    )
                                }, modifier = Modifier.size(60.dp)) {
                                    Icon(
                                        painter = painterResource(R.drawable.complexity_icon),
                                        contentDescription = "",
                                        tint = Color.Red,
                                        modifier = Modifier.size(100.dp)
                                    )
                                }
                                Text(
                                    text = "Сложная", style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 18.sp,
                                        color = Color.Red
                                    )
                                )
                            }

                            else -> {
                                IconButton(onClick = {
                                    viewModel.processIntent(
                                        HomeIntent.SelectTaskComplexity(
                                            TaskComplexity.HARD
                                        )
                                    )
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.complexity_icon),
                                        contentDescription = "",
                                        tint = Color.Red,
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                                Text(
                                    text = "Сложная", style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 16.sp,
                                        color = Color.Red
                                    )
                                )
                            }
                        }


                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(top = 10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.award_img),
                        contentDescription = "awards input",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        "Награда", style = TextStyle(
                            fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Black
                        )
                    )
                }

                Text(
                    text = "Награда за задачу определяется ее уровнем сложности. Для текущей выбранной сложности награда:",
                    style = TextStyle(
                        fontWeight = FontWeight(400),
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(10.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.xp_img),
                            contentDescription = "xp",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            "${state.value.newTask?.complexity?.xp}", style = TextStyle(
                                color = Color.DarkGray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight(900),
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.coin_img),
                            contentDescription = "coins",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            "${state.value.newTask?.complexity?.coins}", style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight(900),
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }


                MainButton(
                    MainBlue,
                    Color.White,
                    { viewModel.processIntent(HomeIntent.AddTask) },
                    "Новая задачу",
                    Icons.Default.Add,
                )

            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth(0.95f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Доброе утро, ${state.value.user?.name}!",
                style = TextStyle(
                    fontWeight = FontWeight(900),
                    fontSize = 25.sp
                ),
                modifier = Modifier.padding(10.dp)
            )

            IconButton(
                modifier = Modifier.size(50.dp),
                onClick = { viewModel.processIntent(HomeIntent.OpenProfile) }) {
                BadgedBox(
                    badge = {
                        if (hasNewMessage.value) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (0).dp, y = (-5).dp)
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Профиль",
                        tint = MainBlue,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

        }

        Card(
            Modifier
                .fillMaxWidth(0.95f),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.elevatedCardElevation(
                20.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush
                            .linearGradient(
                                listOf(Pink40, Pink80),
                            )
                    )
                    .animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.value.tasks != null) {
                    Text(
                        text = "${state.value.tasks?.filter { it.completedAt != null }?.size}/${state.value.tasks?.size}",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)

                    )
                    LinearProgressIndicator(
                        progress = {animatedProgress
                        }, modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.LightGray)
                            .animateContentSize(),
                        color = MainBlue,
                        trackColor = Color.LightGray,
                        gapSize = 0.dp,
                        drawStopIndicator = { false },
                        strokeCap = StrokeCap.Round
                    )
                    Row(
                        Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.fire_icon),
                                tint = Orange,
                                contentDescription = "streak",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                "${state.value.userProgress?.streak} дней",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(900),
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.xp_img),
                                contentDescription = "xp",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                "${
                                    state.value.tasks?.filter { it.completedAt != null }
                                        ?.sumOf { it.complexity.xp }
                                }/${state.value.tasks?.sumOf { it.complexity.xp }}",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(900),
                                    textAlign = TextAlign.Center
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.coin_img),
                                contentDescription = "coins",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                "${
                                    state.value.tasks?.filter { it.completedAt != null }
                                        ?.sumOf { it.complexity.coins }
                                } ",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(900),
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.95f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ежедневные квесты",
                style = TextStyle(
                    fontWeight = FontWeight(600),
                    fontSize = 22.sp, color = Color.Gray
                ),
            )
            IconButton(onClick = { viewModel.processIntent(HomeIntent.ShowAddTaskSheet) }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "add quest",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        LazyColumn(modifier = Modifier.wrapContentHeight()) {
            if (state.value.tasks == null) {
                item {

                    Image(
                        painter = painterResource(R.drawable.no_tasks_image),
                        modifier = Modifier.size(500.dp),
                        contentDescription = "no tasks image"
                    )
                }
            }
            state.value.tasks?.let {
                if (state.value.tasks!!.find { it.completedAt == null } == null) {

                    item {

                        Image(
                            painter = painterResource(R.drawable.all_tasks_completed_image),
                            modifier = Modifier.size(500.dp),
                            contentDescription = "congratulations image"
                        )

                    }

                } else
                    items(
                        state.value.tasks!!.sortedBy { it.completedAt },
                        key = { it.id!! }) { task ->

                        Card(
                            elevation = CardDefaults.elevatedCardElevation(5.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .heightIn(80.dp)
                                .padding(top = 10.dp, bottom = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, top = 10.dp),
                            ) {
                                CircularCheckbox(
                                    task.completedAt != null,
                                    { viewModel.processIntent(HomeIntent.CompleteTask(task.id!!)) },
                                    modifier = Modifier
                                        .size(30.dp),
                                    checkedColor = MainBlue,
                                    uncheckedColor = Color.Transparent,
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .fillMaxWidth(0.75f)
                                ) {
                                    Text(
                                        text = task.name,
                                        style = TextStyle(
                                            fontWeight = FontWeight(400),
                                            fontSize = 20.sp
                                        ),
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {

                                        Icon(
                                            painter = painterResource(R.drawable.complexity_icon),
                                            tint = when (task.complexity) {
                                                TaskComplexity.EASY -> DarkGreen
                                                TaskComplexity.MEDIUM -> CoinColor
                                                TaskComplexity.HARD -> Color.Red
                                            },
                                            contentDescription = "complexity",
                                            modifier = Modifier.size(25.dp)
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(10.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.xp_img),
                                                contentDescription = "xp",
                                                modifier = Modifier.size(30.dp)
                                            )
                                            Text(
                                                task.complexity.xp.toString(),
                                                style = TextStyle(
                                                    color = Color.DarkGray,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight(900),
                                                    textAlign = TextAlign.Center
                                                )
                                            )
                                        }
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(R.drawable.coin_img),
                                        contentDescription = "coins",
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(
                                        task.complexity.coins.toString(),
                                        modifier = Modifier.padding(5.dp),
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(600),
                                            textAlign = TextAlign.Center
                                        )
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
fun CircularCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedColor: Color = MainBlue,
    uncheckedColor: Color = Color.Transparent,
    borderColor: Color = Color.Gray
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) checkedColor else uncheckedColor,
        label = "bg"
    )

    val checkAlpha by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        label = "alpha"
    )

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(0.dp, borderColor, CircleShape)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color.White.copy(alpha = checkAlpha),
            modifier = Modifier
                .size(25.dp)

        )
    }
}
