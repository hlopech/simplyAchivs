package com.example.simplyachivs.presentation.goal.addGoal

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.rememberComposableLambdaN
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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

    val viewModel: AddGoalViewModel = viewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()


    val requester = remember { BringIntoViewRequester() }

    val stepsOffset = 4
    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->

            val fromIndex = from.index - stepsOffset
            val toIndex = to.index - stepsOffset

            if (fromIndex in state.value.steps.indices && toIndex in state.value.steps.indices) {
                viewModel.moveStep(fromIndex, toIndex)
            }
        },
    )
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri })

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

                    reorderState.listState.animateScrollToItem(state.value.steps.size + 2)
                }

                is AddGoalEffect.ShowError -> TODO()

            }
        }

    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(expandedHeight = 50.dp, title = {
                Text("Новая цель")
            }, navigationIcon = {
                IconButton(onClick = { viewModel.processIntent(AddGoalIntent.GoBack) }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = MainBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }, actions = {
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
        }) { paddingValues ->

        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = 100.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .reorderable(reorderState)
                .detectReorderAfterLongPress(reorderState)
//                .padding(top = paddingValues.calculateTopPadding(), bottom = 100.dp)
            ,
            state = reorderState.listState
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    when (selectedImageUri) {

                        null -> Image(
                            painter = painterResource(R.drawable.awards_image),
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(150.dp)
                                .width(300.dp)
                                .padding(bottom = 10.dp)
                                .clip(RoundedCornerShape(20.dp)),

                            )

                        else -> Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(150.dp)
                                .width(300.dp)
                                .padding(bottom = 10.dp)
                                .clip(RoundedCornerShape(20.dp)),

                            )
                    }
                    Button(
                        onClick = {
                            viewModel.processIntent(AddGoalIntent.OpenImagePicker)

                        }, colors = ButtonDefaults.buttonColors(MainBlueLight)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                painter = painterResource(R.drawable.brush_icon),
                                contentDescription = "edit photo",
                                tint = MainBlue,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Изменить иконку цели", style = TextStyle(
                                    fontWeight = FontWeight(500), fontSize = 18.sp, color = MainBlue
                                )
                            )

                        }
                    }


                }

            }
            item {
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
                            "Название цели", style = TextStyle(
                                fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Black
                            )
                        )
                    }
                    OutlinedTextField(
                        value = state.value.goalName,
                        onValueChange = { viewModel.processIntent(AddGoalIntent.ChangeGoalName(it)) },
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
            }

            item {
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
                            painter = painterResource(R.drawable.draw_icon),
                            contentDescription = "target description input",
                            tint = DarkGreen,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            "Описание", style = TextStyle(
                                fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Black
                            )
                        )
                    }
                    OutlinedTextField(
                        value = state.value.goalDescription,
                        onValueChange = {
                            viewModel.processIntent(
                                AddGoalIntent.ChangeGoalDescription(
                                    it
                                )
                            )
                        },
                        placeholder = {
                            Text(
                                "Добавьте описание (необязательно)", style = TextStyle(
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
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(top = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.flag_icon),
                            contentDescription = "target description input",
                            tint = Purple40,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            "Промежуточные цели (шаги)",
                            style = TextStyle(
                                fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Black
                            ),
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color.White)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))

                        ) {

                            Text(
                                "${state.value.steps.size + 1} шаг", style = TextStyle(
                                    fontWeight = FontWeight(900),
                                    fontSize = 18.sp,
                                    color = Color.Black
                                ), modifier = Modifier.padding(10.dp)
                            )
                            VerticalDivider(modifier = Modifier.height(70.dp))

                            TextField(
                                value = state.value.newStepName,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        viewModel.processIntent(
                                            AddGoalIntent.AddNewStep(
                                                state.value.newStepName
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "add step",
                                            tint = MainBlue,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                },
                                onValueChange = {
                                    viewModel.processIntent(
                                        AddGoalIntent.ChangeNewStepName(
                                            it
                                        )
                                    )
                                },
                                placeholder = {
                                    Text(
                                        "Введите подцель...", style = TextStyle(
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
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White,

                                    )
                            )
                        }
                    }
                }
            }

            items(state.value.steps, key = { it.id }) { step ->
                ReorderableItem(
                    reorderState,
                    modifier = Modifier.height(70.dp),
                    key = step.id
                ) { isDragging ->
                    StepCard(
                        step = step,
                        {
                            viewModel.processIntent(
                                AddGoalIntent.DeleteStep(
                                    step.id,
                                    step.position
                                )
                            )
                        },
                        modifier = Modifier
                            .shadow(if (isDragging) 8.dp else 2.dp)
                            .animateItemPlacement()
//                            .pointerInput(Unit) {
//                                detectDragGestures (
//                                    onDragStart = {
//                                    },
//                                    onDrag = { _, _ -> }, // нужно для захвата
//                                    onDragEnd = {},
//                                    onDragCancel = {}
//                                )
//                            }
                    )
                }
            }


            item {
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
                    text = "В зависимости от выбранной сложности, при выполнении цели полностью, будет начисленна награда, чем вышел сложность, тем выше награда.",
                    style = TextStyle(
                        fontWeight = FontWeight(400),
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround

                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        when (state.value.complexity) {
                            GoalComplexity.EASY -> {
                                IconButton(
                                    onClick = {
                                        viewModel.processIntent(
                                            AddGoalIntent.SelectGoalComplexity(
                                                GoalComplexity.EASY
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
                                        AddGoalIntent.SelectGoalComplexity(
                                            GoalComplexity.EASY
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
                        when (state.value.complexity) {
                            GoalComplexity.MEDIUM -> {
                                IconButton(
                                    onClick = {
                                        viewModel.processIntent(
                                            AddGoalIntent.SelectGoalComplexity(
                                                GoalComplexity.MEDIUM
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
                                            AddGoalIntent.SelectGoalComplexity(
                                                GoalComplexity.MEDIUM
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
                        when (state.value.complexity) {
                            GoalComplexity.HARD -> {
                                IconButton(onClick = {
                                    viewModel.processIntent(
                                        AddGoalIntent.SelectGoalComplexity(
                                            GoalComplexity.HARD
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
                                        AddGoalIntent.SelectGoalComplexity(
                                            GoalComplexity.HARD
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


            }

            item {
                if (state.value.complexity != null) {

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
                        text = "Награда за цель определяется ее уровнем сложности. Для текущей выбранной сложности награда:",
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
                            .bringIntoViewRequester(requester)
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
                                "${state.value.complexity?.xp}", style = TextStyle(
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
                                "${state.value.complexity?.coins}", style = TextStyle(
                                    color = Color.Black,
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


    }


}


