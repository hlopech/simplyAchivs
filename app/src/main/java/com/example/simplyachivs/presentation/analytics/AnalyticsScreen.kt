package com.example.simplyachivs.presentation.analytics

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplyachivs.domain.model.analytics.AnalyticsData
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.MainBlue
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

// ─────────────────────────────────────────────────────────────
// Colours
// ─────────────────────────────────────────────────────────────
private val CardBg = Color(0xFFF8F9FF)
private val SectionTitle = Color(0xFF1A1A2E)
private val SubText = Color(0xFF888888)
private val EasyColor = DarkGreen
private val MediumColor = CoinColor
private val HardColor = Color(0xFFE53935)
private val NightColor = Color(0xFF7C4DFF)
private val MorningColor = Color(0xFFFF9800)
private val DayTimeColor = Color(0xFF2196F3)
private val EveningColor = Color(0xFF3F51B5)

// Moved outside composable to avoid Compose compiler issues
private data class TimeSlot(
    val label: String,
    val emoji: String,
    val hours: IntRange,
    val color: Color,
)

private val timeSlots = listOf(
    TimeSlot("Ночь", "🌙", 0..5, NightColor),
    TimeSlot("Утро", "🌅", 6..11, MorningColor),
    TimeSlot("День", "☀️", 12..17, DayTimeColor),
    TimeSlot("Вечер", "🌆", 18..23, EveningColor),
)

private val complexityItems = listOf(
    Triple("EASY", "Лёгкие", EasyColor),
    Triple("MEDIUM", "Средние", MediumColor),
    Triple("HARD", "Сложные", HardColor),
)

// ─────────────────────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(onBack: () -> Unit) {
    val viewModel: AnalyticsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 50.dp,
                title = { Text("Аналитика", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Назад",
                            tint = MainBlue,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                },
            )
        },
    ) { padding ->
        when (val state = uiState) {
            is AnalyticsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator(color = MainBlue) }
            }
            is AnalyticsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) { Text(state.message, color = Color.Gray) }
            }
            is AnalyticsUiState.Content -> {
                AnalyticsContent(data = state.data, modifier = Modifier.padding(padding))
            }
        }
    }
}

@Composable
private fun AnalyticsContent(data: AnalyticsData, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { SummaryRow(data) }
        item { DayOfWeekSection(data.tasksByDayOfWeek) }
        item { HourSection(data.tasksByHour) }
        item { HeatmapSection(data.tasksByDate) }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CompletionRateCard(data.completionRate, Modifier.weight(1f))
                ComplexityCard(data.complexityBreakdown, Modifier.weight(1f))
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

// ─────────────────────────────────────────────────────────────
// Summary — 3 stat tiles
// ─────────────────────────────────────────────────────────────
@Composable
private fun SummaryRow(data: AnalyticsData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatTile(
            value = data.totalTasksCompleted.toString(),
            label = "Задач\nвыполнено",
            gradient = listOf(Color(0xFF1976D2), MainBlue),
            modifier = Modifier.weight(1f),
        )
        StatTile(
            value = data.totalGoalsCompleted.toString(),
            label = "Целей\nдостигнуто",
            gradient = listOf(Color(0xFF2E7D32), DarkGreen),
            modifier = Modifier.weight(1f),
        )
        StatTile(
            value = data.totalStepsCompleted.toString(),
            label = "Шагов\nвыполнено",
            gradient = listOf(Color(0xFF6A1B9A), Color(0xFFAB47BC)),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatTile(
    value: String,
    label: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(gradient))
            .padding(vertical = 16.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Bar chart — day of week
// ─────────────────────────────────────────────────────────────
@Composable
private fun DayOfWeekSection(tasksByDayOfWeek: Map<DayOfWeek, Int>) {
    val days = DayOfWeek.values()
    val values = days.map { tasksByDayOfWeek[it] ?: 0 }
    val maxVal = values.maxOrNull()?.coerceAtLeast(1) ?: 1
    val maxValue = values.maxOrNull() ?: 0
    val bestIdx = if (maxValue > 0) values.indexOf(maxValue) else -1
    val ruLabels = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

    AnalyticsCard(title = "По дням недели", subtitle = "Когда вы наиболее продуктивны") {
        Row(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
        ) {
            values.forEachIndexed { i, v ->
                val fraction = v.toFloat() / maxVal
                var target by remember { mutableFloatStateOf(0f) }
                LaunchedEffect(v) { target = fraction }
                val anim by animateFloatAsState(
                    targetValue = target,
                    animationSpec = tween(800, delayMillis = i * 60, easing = FastOutSlowInEasing),
                    label = "dow_$i",
                )
                val isHighlight = i == bestIdx
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                ) {
                    if (v > 0) {
                        Text(
                            text = v.toString(),
                            fontSize = 9.sp,
                            color = if (isHighlight) CoinColor else SubText,
                            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(anim.coerceAtLeast(0.03f))
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    if (isHighlight)
                                        Brush.verticalGradient(listOf(CoinColor, Color(0xFFFF6F00)))
                                    else
                                        Brush.verticalGradient(listOf(MainBlue, Color(0xFF1565C0))),
                                ),
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = ruLabels[i],
                        fontSize = 10.sp,
                        color = if (isHighlight) CoinColor else SubText,
                    )
                }
            }
        }

        if (bestIdx >= 0) {
            val bestLabel = days[bestIdx].getDisplayName(TextStyle.FULL, Locale("ru"))
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Лучший день: $bestLabel",
                fontSize = 12.sp,
                color = CoinColor,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Bar chart — time of day (4 slots)
// ─────────────────────────────────────────────────────────────
@Composable
private fun HourSection(tasksByHour: Map<Int, Int>) {
    val slotValues = timeSlots.map { s -> s.hours.sumOf { h -> tasksByHour[h] ?: 0 } }
    val maxVal = slotValues.maxOrNull()?.coerceAtLeast(1) ?: 1
    val maxValue = slotValues.maxOrNull() ?: 0
    val bestIdx = if (maxValue > 0) slotValues.indexOf(maxValue) else -1

    AnalyticsCard(title = "Время активности", subtitle = "В какое время суток вы работаете") {
        Row(
            modifier = Modifier.fillMaxWidth().height(130.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
        ) {
            slotValues.forEachIndexed { i, v ->
                val slot = timeSlots[i]
                val fraction = v.toFloat() / maxVal
                var target by remember { mutableFloatStateOf(0f) }
                LaunchedEffect(v) { target = fraction }
                val anim by animateFloatAsState(
                    targetValue = target,
                    animationSpec = tween(900, delayMillis = i * 80, easing = FastOutSlowInEasing),
                    label = "hour_$i",
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                ) {
                    if (v > 0) {
                        Text(
                            text = v.toString(),
                            fontSize = 10.sp,
                            color = slot.color,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(anim.coerceAtLeast(0.03f))
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(slot.color, slot.color.copy(alpha = 0.5f)),
                                    ),
                                ),
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(text = slot.emoji, fontSize = 16.sp)
                    Text(
                        text = slot.label,
                        fontSize = 10.sp,
                        color = slot.color,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        if (bestIdx >= 0) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Пик активности: ${timeSlots[bestIdx].label.lowercase()}",
                fontSize = 12.sp,
                color = timeSlots[bestIdx].color,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Heatmap — last 14 days (2 rows × 7 cols)
// ─────────────────────────────────────────────────────────────
@Composable
private fun HeatmapSection(tasksByDate: Map<LocalDate, Int>) {
    val today = LocalDate.now()
    val dates = (13 downTo 0).map { today.minusDays(it.toLong()) }
    val maxVal = tasksByDate.values.maxOrNull()?.coerceAtLeast(1) ?: 1

    AnalyticsCard(title = "Последние 14 дней", subtitle = "Интенсивность выполненных задач") {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(dates.take(7), dates.drop(7)).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    week.forEach { date ->
                        val count = tasksByDate[date] ?: 0
                        val intensity = count.toFloat() / maxVal
                        val cellColor = when {
                            count == 0 -> Color(0xFFEEEEEE)
                            intensity < 0.34f -> MainBlue.copy(alpha = 0.35f)
                            intensity < 0.67f -> MainBlue.copy(alpha = 0.65f)
                            else -> MainBlue
                        }
                        val isToday = date == today
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(cellColor)
                                .padding(if (isToday) 1.dp else 0.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    fontSize = 9.sp,
                                    color = if (count == 0) Color.Gray else Color.White,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                )
                                if (count > 0) {
                                    Text(
                                        text = count.toString(),
                                        fontSize = 8.sp,
                                        color = Color.White.copy(alpha = 0.85f),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(text = "Меньше", fontSize = 10.sp, color = SubText)
            listOf(
                Color(0xFFEEEEEE),
                MainBlue.copy(alpha = 0.35f),
                MainBlue.copy(alpha = 0.65f),
                MainBlue,
            ).forEach { c ->
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(c),
                )
            }
            Text(text = "Больше", fontSize = 10.sp, color = SubText)
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Donut — completion rate
// ─────────────────────────────────────────────────────────────
@Composable
private fun CompletionRateCard(rate: Float, modifier: Modifier = Modifier) {
    AnalyticsCard(
        title = "Выполнение",
        subtitle = "Задачи без отмены",
        modifier = modifier,
    ) {
        var target by remember { mutableFloatStateOf(0f) }
        LaunchedEffect(rate) { target = rate }
        val anim by animateFloatAsState(
            targetValue = target,
            animationSpec = tween(1200, easing = FastOutSlowInEasing),
            label = "rate",
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = size.width * 0.16f
                    val inset = stroke / 2
                    val arcSize = Size(size.width - stroke, size.height - stroke)
                    val topLeft = Offset(inset, inset)
                    drawArc(
                        color = Color(0xFFE0E0E0),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        size = arcSize,
                        topLeft = topLeft,
                    )
                    drawArc(
                        color = DarkGreen,
                        startAngle = -90f,
                        sweepAngle = anim * 360f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        size = arcSize,
                        topLeft = topLeft,
                    )
                }
                Text(
                    text = "${(anim * 100).roundToInt()}%",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = DarkGreen,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Complexity breakdown — horizontal bars
// ─────────────────────────────────────────────────────────────
@Composable
private fun ComplexityCard(breakdown: Map<String, Int>, modifier: Modifier = Modifier) {
    val maxVal = breakdown.values.maxOrNull()?.coerceAtLeast(1) ?: 1

    AnalyticsCard(title = "Сложность", subtitle = "Выполненных задач", modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            complexityItems.forEach { (key, label, color) ->
                val count = breakdown[key] ?: 0
                val fraction = count.toFloat() / maxVal
                var target by remember { mutableFloatStateOf(0f) }
                LaunchedEffect(count) { target = fraction }
                val anim by animateFloatAsState(
                    targetValue = target,
                    animationSpec = tween(900, easing = FastOutSlowInEasing),
                    label = "cx_$key",
                )
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = label, fontSize = 11.sp, color = SectionTitle)
                        Text(
                            text = count.toString(),
                            fontSize = 11.sp,
                            color = color,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFEEEEEE)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(anim.coerceAtLeast(if (count > 0) 0.04f else 0f))
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(50))
                                .background(
                                    Brush.horizontalGradient(listOf(color.copy(alpha = 0.6f), color)),
                                ),
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Shared card container
// ─────────────────────────────────────────────────────────────
@Composable
private fun AnalyticsCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardBg)
            .padding(16.dp),
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SectionTitle)
        Text(text = subtitle, fontSize = 11.sp, color = SubText, lineHeight = 14.sp)
        Spacer(Modifier.height(12.dp))
        content()
    }
}
