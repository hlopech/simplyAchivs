package com.example.simplyachivs.presentation.shop.addAward

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplyachivs.R
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.MainBlueLight
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAwardScreen(onBack: () -> Unit) {

    val viewModel: AddAwardViewModel = hiltViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val min = 1
    val max = 9999

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                AddAwardEffect.NavigateToAwards -> onBack()
                is AddAwardEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 50.dp,
                title = { Text("Новая награда", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = MainBlue,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.processIntent(AddAwardIntent.AddNewAward) }) {
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding(), bottom = 40.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Preview ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(listOf(MainBlueLight, Color.White))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(state.value.selectedImage.resId),
                        contentDescription = "",
                        modifier = Modifier.size(90.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Name ──────────────────────────────────────────────────
            SectionCard {
                SectionHeader(
                    icon = { Icon(painterResource(R.drawable.star_icon), null, tint = CoinColor, modifier = Modifier.size(22.dp)) },
                    title = "Название"
                )
                val awardNameError = state.value.awardNameError
                OutlinedTextField(
                    value = state.value.awardName,
                    onValueChange = { viewModel.processIntent(AddAwardIntent.ChangeAwardName(it)) },
                    placeholder = { Text("Введите название...", color = Color.Gray) },
                    isError = awardNameError != null,
                    supportingText = {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (awardNameError != null) {
                                Text(awardNameError, color = Color.Red, fontSize = 12.sp)
                            } else {
                                Spacer(Modifier.weight(1f))
                            }
                            Text(
                                "${state.value.awardName.length}/50",
                                color = if (state.value.awardName.length >= 45) Color.Red else Color.Gray,
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

            // ── Description ───────────────────────────────────────────
            SectionCard {
                SectionHeader(
                    icon = { Icon(painterResource(R.drawable.draw_icon), null, tint = DarkGreen, modifier = Modifier.size(22.dp)) },
                    title = "Описание"
                )
                OutlinedTextField(
                    value = state.value.awardDescription,
                    onValueChange = { viewModel.processIntent(AddAwardIntent.ChangeAwardDescription(it)) },
                    placeholder = { Text("Добавьте описание (необязательно)", color = Color.Gray) },
                    supportingText = {
                        Text(
                            "${state.value.awardDescription.length}/200",
                            color = if (state.value.awardDescription.length >= 180) Color.Red else Color.Gray,
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

            // ── Image picker ─────────────────────────────────────────
            SectionCard {
                SectionHeader(
                    icon = { Icon(painterResource(R.drawable.brush_icon), null, tint = MainBlue, modifier = Modifier.size(22.dp)) },
                    title = "Картинка"
                )
                Spacer(Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(AwardImage.entries) { awardImage ->
                        val isSelected = state.value.selectedImage == awardImage
                        Card(
                            onClick = { viewModel.processIntent(AddAwardIntent.SelectGoalImage(awardImage)) },
                            shape = RoundedCornerShape(14.dp),
                            border = if (isSelected) BorderStroke(2.dp, MainBlue) else BorderStroke(1.dp, LightGray),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MainBlue.copy(alpha = 0.08f) else Color(0xFFFAFAFA)
                            ),
                            elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 0.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(awardImage.resId),
                                    contentDescription = awardImage.label,
                                    modifier = Modifier.size(52.dp)
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = awardImage.label,
                                    fontSize = 11.sp,
                                    color = if (isSelected) MainBlue else Color.Gray,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // ── Price ─────────────────────────────────────────────────
            SectionCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SectionHeader(
                        icon = { Image(painterResource(R.drawable.coin_img), null, modifier = Modifier.size(22.dp)) },
                        title = "Стоимость"
                    )
                    // Current price badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CoinColor.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Image(painterResource(R.drawable.coin_img), null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = state.value.price.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB8860B)
                        )
                    }
                }

                Slider(
                    value = state.value.price.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = MainBlue,
                        activeTrackColor = MainBlue,
                        inactiveTrackColor = LightGray
                    ),
                    onValueChange = { v ->
                        viewModel.processIntent(AddAwardIntent.ChangeAwardPriceSlider(v.toInt().coerceIn(min, max)))
                    },
                    valueRange = min.toFloat()..max.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$min", fontSize = 12.sp, color = Color.Gray)
                    Text("$max", fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(Modifier.height(8.dp))

                val priceError = state.value.priceError
                OutlinedTextField(
                    value = state.value.price.toString(),
                    onValueChange = { text ->
                        val parsed = text.filter { it.isDigit() }.toIntOrNull() ?: 0
                        viewModel.processIntent(AddAwardIntent.ChangeAwardPrice(parsed.coerceIn(0, max)))
                    },
                    placeholder = { Text("Введите стоимость...", color = Color.Gray) },
                    isError = priceError != null,
                    supportingText = if (priceError != null) {
                        { Text(priceError, color = Color.Red, fontSize = 12.sp) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
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
private fun SectionHeader(icon: @Composable () -> Unit, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(Modifier.width(8.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
    Spacer(Modifier.height(8.dp))
}
