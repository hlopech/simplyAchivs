package com.example.simplyachivs.presentation.achievements

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplyachivs.domain.model.achievement.Achievement
import com.example.simplyachivs.domain.model.achievement.AchievementStatus
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.MainBlue

private val GoldColor = Color(0xFFFFD700)
private val GoldDark = Color(0xFFC8960C)
private val SecretColor = Color(0xFF7C4DFF)
private val SecretLight = Color(0xFFEDE7FF)
private val CompletedBg = Color(0xFFFFF8E1)
private val InProgressBg = Color(0xFFE3F2FD)
private val LockedBg = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(onBack: () -> Unit) {
    val viewModel: AchievementsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                expandedHeight = 50.dp,
                title = { Text("Достижения", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Назад",
                            tint = MainBlue,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is AchievementsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MainBlue)
                }
            }

            is AchievementsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message, color = Color.Gray)
                }
            }

            is AchievementsUiState.Content -> {
                val completed = state.achievements.count { it.status == AchievementStatus.COMPLETED }
                val total = state.achievements.size

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AchievementsHeader(completed = completed, total = total)
                        Spacer(Modifier.height(4.dp))
                    }
                    items(state.achievements, key = { it.id }) { achievement ->
                        AchievementCard(achievement = achievement)
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementsHeader(completed: Int, total: Int) {
    val progress = if (total == 0) 0f else completed.toFloat() / total
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "headerProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF1A237E), Color(0xFF283593), MainBlue)
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ваш прогресс",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "$completed / $total",
                    color = GoldColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = GoldColor,
                trackColor = Color.White.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (completed == total && total > 0) "Все достижения открыты! 🏆"
                       else "Продолжайте — вас ждут новые награды!",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    val isCompleted = achievement.status == AchievementStatus.COMPLETED
    val isSecret = achievement.isSecret
    val isLocked = achievement.status == AchievementStatus.LOCKED

    // Pulsing glow animation for completed achievements
    val infiniteTransition = rememberInfiniteTransition(label = "glow_${achievement.id}")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha_${achievement.id}"
    )

    // Animate progress bar
    var progressTarget by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(achievement.id) { progressTarget = achievement.progress }
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "progress_${achievement.id}"
    )

    val cardBg = when {
        isCompleted -> CompletedBg
        isSecret && isLocked -> SecretLight
        isLocked -> LockedBg
        else -> InProgressBg
    }

    val borderColor = when {
        isCompleted -> GoldColor
        isSecret && isLocked -> SecretColor
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isCompleted)
                    Brush.linearGradient(listOf(CompletedBg, Color(0xFFFFF3C4)))
                else
                    Brush.linearGradient(listOf(cardBg, cardBg))
            )
    ) {
        // Gold shimmer border for completed
        if (isCompleted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        Brush.horizontalGradient(listOf(GoldDark, GoldColor, GoldDark)),
                        alpha = glowAlpha
                    )
            )
        }

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon area
            AchievementIcon(achievement = achievement, glowAlpha = glowAlpha)

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = achievement.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = when {
                            isCompleted -> GoldDark
                            isSecret && isLocked -> SecretColor
                            isLocked -> Color.Gray
                            else -> Color(0xFF1A1A1A)
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (isCompleted) {
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "✓",
                            color = DarkGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.alpha(glowAlpha)
                        )
                    }
                }

                Spacer(Modifier.height(3.dp))

                Text(
                    text = achievement.description,
                    fontSize = 12.sp,
                    color = if (isLocked) Color.Gray else Color(0xFF555555),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                if (!isCompleted && achievement.status == AchievementStatus.IN_PROGRESS) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(50)),
                        color = MainBlue,
                        trackColor = Color(0xFFBBDEFB),
                        strokeCap = StrokeCap.Round,
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = achievement.progressText,
                        fontSize = 11.sp,
                        color = MainBlue,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (isCompleted) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = achievement.progressText,
                        fontSize = 11.sp,
                        color = GoldDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AchievementIcon(achievement: Achievement, glowAlpha: Float) {
    val isCompleted = achievement.status == AchievementStatus.COMPLETED
    val isLocked = achievement.status == AchievementStatus.LOCKED
    val isSecret = achievement.isSecret

    val bgColor = when {
        isCompleted -> Brush.radialGradient(listOf(GoldColor, GoldDark))
        isSecret && isLocked -> Brush.radialGradient(listOf(SecretColor, Color(0xFF4527A0)))
        isLocked -> Brush.radialGradient(listOf(Color(0xFFBDBDBD), Color(0xFF9E9E9E)))
        else -> Brush.radialGradient(listOf(MainBlue, Color(0xFF1565C0)))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (isCompleted) Modifier.scale(1f + (glowAlpha - 0.7f) * 0.05f)
                else Modifier
            )
    ) {
        if (isSecret && isLocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        } else {
            Icon(
                painter = painterResource(achievement.iconRes),
                contentDescription = null,
                tint = Color.White.copy(alpha = if (isLocked) 0.6f else 1f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
