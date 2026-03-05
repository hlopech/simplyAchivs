package com.example.simplyachivs.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.domain.model.achievement.Achievement

private val GoldColor = Color(0xFFFFD700)
private val GoldDark = Color(0xFFC8960C)

@Composable
fun AchievementUnlockedBanner(
    achievement: Achievement?,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible && achievement != null,
        modifier = modifier,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(400)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(350)
        ) + fadeOut(animationSpec = tween(250)),
    ) {
        if (achievement == null) return@AnimatedVisibility

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF1A237E), Color(0xFF283593))
                    )
                )
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // Icon circle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(listOf(GoldColor, GoldDark))
                    ),
            ) {
                Icon(
                    painter = painterResource(achievement.iconRes),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            androidx.compose.foundation.layout.Column {
                Text(
                    text = "Достижение разблокировано!",
                    color = GoldColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 0.3.sp,
                )
                Text(
                    text = achievement.title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                )
            }

            Spacer(Modifier.width(6.dp))

            Text(text = "🏆", fontSize = 20.sp)
        }
    }
}
