@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.simplyachivs.presentation.asceticism

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplyachivs.domain.model.asceticism.AsceticismPreset
import com.example.simplyachivs.domain.model.asceticism.AsceticismStatus
import com.example.simplyachivs.domain.model.asceticism.AsceticismWithProgress
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.MainBlueLight
import java.time.LocalDate
import java.util.UUID

private val QUICK_DURATIONS = listOf(7, 14, 21, 30, 66)
private val EMOJI_OPTIONS = listOf(
    "💪", "🧘", "🌊", "📵", "🍬", "📚", "🏃", "🌅",
    "💧", "📱", "🙏", "🚫", "🎯", "⚡", "🔥", "🌿",
)

@Composable
fun AsceticismScreen(onBack: () -> Unit) {
    val viewModel: AsceticismViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AsceticismEffect.GoBack -> onBack()
                is AsceticismEffect.ShowError -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 50.dp,
                title = { Text("Аскеза") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = MainBlue,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                },
            )
        }
    ) { padding ->
        when (val s = state) {
            is AsceticismUiState.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator(color = MainBlue) }

            is AsceticismUiState.Error -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { Text(s.message, color = MaterialTheme.colorScheme.error) }

            is AsceticismUiState.Content -> {
                if (s.showCustomDialog) {
                    CustomAsceticismDialog(
                        onDismiss = { viewModel.processIntent(AsceticismIntent.CloseCustomDialog) },
                        onConfirm = { title, desc, emoji, days ->
                            viewModel.processIntent(AsceticismIntent.CreateCustom(title, desc, emoji, days))
                        },
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding()),
                ) {
                    TabRow(
                        selectedTabIndex = s.tab,
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MainBlue,
                        indicator = { tabPositions ->
                            if (s.tab < tabPositions.size) {
                                Box(
                                    Modifier
                                        .tabIndicatorOffset(tabPositions[s.tab])
                                        .height(3.dp)
                                        .background(
                                            MainBlue,
                                            RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
                                        )
                                )
                            }
                        },
                    ) {
                        listOf("Активные", "Выбрать", "История").forEachIndexed { index, label ->
                            Tab(
                                selected = s.tab == index,
                                onClick = { viewModel.processIntent(AsceticismIntent.SelectTab(index)) },
                                selectedContentColor = MainBlue,
                                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ) {
                                Text(
                                    label,
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    fontSize = 14.sp,
                                    fontWeight = if (s.tab == index) FontWeight.SemiBold else FontWeight.Normal,
                                )
                            }
                        }
                    }

                    when (s.tab) {
                        0 -> ActiveTab(
                            items = s.activeItems,
                            onCheckIn = { viewModel.processIntent(AsceticismIntent.CheckIn(it)) },
                            onAbandon = { viewModel.processIntent(AsceticismIntent.Abandon(it)) },
                        )
                        1 -> ChooseTab(
                            presets = s.presets,
                            onStart = { preset, days ->
                                viewModel.processIntent(AsceticismIntent.StartPreset(preset, days))
                            },
                            onOpenCustom = { viewModel.processIntent(AsceticismIntent.OpenCustomDialog) },
                        )
                        2 -> HistoryTab(items = s.historyItems)
                    }
                }
            }
        }
    }
}

// ── Active Tab ────────────────────────────────────────────────────────────────

@Composable
private fun ActiveTab(
    items: List<AsceticismWithProgress>,
    onCheckIn: (UUID) -> Unit,
    onAbandon: (UUID) -> Unit,
) {
    if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🧘", fontSize = 56.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    "Нет активных аскез",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Перейдите в «Выбрать», чтобы начать",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items) { wp -> ActiveAsceticismCard(wp, onCheckIn, onAbandon) }
    }
}

@Composable
private fun ActiveAsceticismCard(
    wp: AsceticismWithProgress,
    onCheckIn: (UUID) -> Unit,
    onAbandon: (UUID) -> Unit,
) {
    val asc = wp.asceticism
    var showAbandonConfirm by remember { mutableStateOf(false) }

    if (showAbandonConfirm) {
        AlertDialog(
            onDismissRequest = { showAbandonConfirm = false },
            title = { Text("Отказаться от аскезы?") },
            text = { Text("Весь прогресс будет потерян") },
            confirmButton = {
                TextButton(onClick = { showAbandonConfirm = false; onAbandon(asc.id) }) {
                    Text("Отказаться", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAbandonConfirm = false }) { Text("Отмена") }
            },
        )
    }

    val animatedProgress by animateFloatAsState(
        targetValue = asc.progress,
        animationSpec = tween(600),
        label = "progress",
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MainBlueLight.copy(alpha = 0.5f)),
                ) {
                    Text(asc.emoji, fontSize = 28.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        asc.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        "День ${asc.daysElapsed} из ${asc.durationDays}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                // Streak badge
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFF6B00).copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text("🔥", fontSize = 18.sp)
                    Text(
                        "${wp.currentStreak}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B00),
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MainBlue,
                trackColor = MainBlue.copy(alpha = 0.15f),
            )
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "${(asc.progress * 100).toInt()}%",
                    fontSize = 11.sp,
                    color = MainBlue,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    "Осталось ${asc.daysRemaining} дн.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(12.dp))
            MiniCalendar(wp)
            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { showAbandonConfirm = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Отказаться", fontSize = 13.sp) }

                Button(
                    onClick = { onCheckIn(asc.id) },
                    modifier = Modifier.weight(1f),
                    enabled = !wp.checkedInToday,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainBlue,
                        disabledContainerColor = Color(0xFF4CAF50),
                        disabledContentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    if (wp.checkedInToday) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Выполнено", fontSize = 13.sp)
                    } else {
                        Text("Отметить", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniCalendar(wp: AsceticismWithProgress) {
    val today = LocalDate.now()
    val checkedDays = wp.checkIns.map { it.epochDay }.toSet()
    val startDay = wp.asceticism.startEpochDay
    val days = (6 downTo 0).map { today.minusDays(it.toLong()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        days.forEach { date ->
            val epochDay = date.toEpochDay()
            val isBeforeStart = epochDay < startDay
            val isChecked = checkedDays.contains(epochDay)
            val isToday = date == today

            val bgColor = when {
                isBeforeStart -> Color.Transparent
                isChecked -> Color(0xFF4CAF50)
                isToday -> MainBlue.copy(alpha = 0.15f)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            // Bug fix: today unChecked → use MainBlue text (not white) on light background
            val textColor = when {
                isBeforeStart -> Color.Transparent
                isChecked -> Color.White
                isToday -> MainBlue
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    date.dayOfWeek.name.take(2),
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(bgColor)
                        .then(
                            if (isToday && !isChecked)
                                Modifier.border(1.5.dp, MainBlue, CircleShape)
                            else Modifier
                        ),
                ) {
                    if (!isBeforeStart) {
                        Text(
                            "${date.dayOfMonth}",
                            fontSize = 11.sp,
                            color = textColor,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }
}

// ── Choose Tab ────────────────────────────────────────────────────────────────

@Composable
private fun ChooseTab(
    presets: List<AsceticismPreset>,
    onStart: (AsceticismPreset, Int) -> Unit,
    onOpenCustom: () -> Unit,
) {
    var selectedPreset by remember { mutableStateOf<AsceticismPreset?>(null) }

    selectedPreset?.let { preset ->
        DurationPickerDialog(
            preset = preset,
            onDismiss = { selectedPreset = null },
            onConfirm = { days -> onStart(preset, days); selectedPreset = null },
        )
    }

    val categories = presets.map { it.category }.distinct()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            // "Own asceticism" card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MainBlue.copy(alpha = 0.08f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MainBlue.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .clickable { onOpenCustom() },
                elevation = CardDefaults.cardElevation(0.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MainBlue.copy(alpha = 0.15f)),
                    ) { Text("✨", fontSize = 26.sp) }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("Своя аскеза", fontWeight = FontWeight.Bold, color = MainBlue, fontSize = 15.sp)
                        Text(
                            "Создайте собственный вызов",
                            fontSize = 13.sp,
                            color = MainBlue.copy(alpha = 0.65f),
                        )
                    }
                }
            }
        }

        categories.forEach { category ->
            item {
                Text(
                    category,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
                )
            }
            val categoryPresets = presets.filter { it.category == category }
            items(categoryPresets) { preset ->
                PresetCard(preset = preset, onClick = { selectedPreset = preset })
            }
        }
    }
}

@Composable
private fun PresetCard(preset: AsceticismPreset, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MainBlueLight.copy(alpha = 0.4f)),
            ) { Text(preset.emoji, fontSize = 24.sp) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(preset.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(
                    preset.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${preset.suggestedDays} дней",
                    fontSize = 11.sp,
                    color = MainBlue,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

// ── Dialogs ───────────────────────────────────────────────────────────────────

@Composable
private fun DurationPickerDialog(
    preset: AsceticismPreset,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    var days by remember { mutableStateOf(preset.suggestedDays.toString()) }
    val daysInt = days.toIntOrNull() ?: 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(preset.emoji, fontSize = 24.sp)
                Spacer(Modifier.width(8.dp))
                Text(preset.title, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    preset.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(16.dp))
                Text("Продолжительность", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                // Quick chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    QUICK_DURATIONS.forEach { d ->
                        FilterChip(
                            selected = daysInt == d,
                            onClick = { days = d.toString() },
                            label = { Text("${d}д", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MainBlue,
                                selectedLabelColor = Color.White,
                            ),
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = days,
                    onValueChange = { new ->
                        if (new.length <= 3) days = new.filter { it.isDigit() }
                    },
                    label = { Text("Другое количество дней") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    suffix = { Text("дн.") },
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(daysInt) },
                enabled = daysInt in 1..365,
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                shape = RoundedCornerShape(10.dp),
            ) { Text("Начать") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
    )
}

@Composable
private fun CustomAsceticismDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("💪") }
    var days by remember { mutableStateOf("30") }
    val daysInt = days.toIntOrNull() ?: 0
    val canConfirm = title.isNotBlank() && daysInt in 1..365

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Title
                Text(
                    "Своя аскеза",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Spacer(Modifier.height(16.dp))

                // Selected emoji preview
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MainBlueLight.copy(alpha = 0.35f)),
                ) {
                    Text(emoji, fontSize = 42.sp)
                }
                Spacer(Modifier.height(10.dp))

                // Emoji picker row
                Text(
                    "Выберите иконку",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    EMOJI_OPTIONS.forEach { e ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (emoji == e) MainBlue.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                )
                                .border(
                                    width = if (emoji == e) 1.5.dp else 0.dp,
                                    color = if (emoji == e) MainBlue else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp),
                                )
                                .clickable { emoji = e },
                        ) {
                            Text(e, fontSize = 20.sp)
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))

                // Name field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )
                Spacer(Modifier.height(10.dp))

                // Description field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание (необязательно)") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )
                Spacer(Modifier.height(14.dp))

                // Duration
                Text(
                    "Продолжительность",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    QUICK_DURATIONS.forEach { d ->
                        FilterChip(
                            selected = daysInt == d,
                            onClick = { days = d.toString() },
                            label = { Text("${d}д", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MainBlue,
                                selectedLabelColor = Color.White,
                            ),
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = days,
                    onValueChange = { new ->
                        if (new.length <= 3) days = new.filter { it.isDigit() }
                    },
                    label = { Text("Другое") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    suffix = { Text("дн.") },
                    shape = RoundedCornerShape(12.dp),
                )

                Spacer(Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                    ) { Text("Отмена") }

                    Button(
                        onClick = { onConfirm(title.trim(), description.trim(), emoji, daysInt) },
                        enabled = canConfirm,
                        modifier = Modifier.weight(2f),
                        colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                        shape = RoundedCornerShape(12.dp),
                    ) { Text("Создать") }
                }
            }
        }
    }
}

// ── History Tab ───────────────────────────────────────────────────────────────

@Composable
private fun HistoryTab(items: List<AsceticismWithProgress>) {
    if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📜", fontSize = 56.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    "История пуста",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(items) { wp -> HistoryCard(wp) }
    }
}

@Composable
private fun HistoryCard(wp: AsceticismWithProgress) {
    val asc = wp.asceticism
    val statusColor = when (asc.status) {
        AsceticismStatus.COMPLETED -> Color(0xFF4CAF50)
        AsceticismStatus.ABANDONED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val statusText = when (asc.status) {
        AsceticismStatus.COMPLETED -> "Выполнено"
        AsceticismStatus.ABANDONED -> "Заброшено"
        else -> ""
    }
    val statusEmoji = when (asc.status) {
        AsceticismStatus.COMPLETED -> "✅"
        AsceticismStatus.ABANDONED -> "❌"
        else -> ""
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) { Text(asc.emoji, fontSize = 24.sp) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(asc.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(
                    "${wp.totalCheckedIn} из ${asc.durationDays} дней",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(statusEmoji, fontSize = 18.sp)
                Text(statusText, fontSize = 11.sp, color = statusColor, fontWeight = FontWeight.Medium)
            }
        }
    }
}
