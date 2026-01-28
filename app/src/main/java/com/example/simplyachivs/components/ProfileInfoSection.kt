package com.example.simplyachivs.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.R
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.LightGreen
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.Orange
import com.example.simplyachivs.ui.theme.ProfileComponentsBg
import kotlinx.serialization.json.Json

@Preview
@Composable
fun ProfileInfoSection() {

    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedCard(
                shape = RoundedCornerShape(999.dp),
                border = BorderStroke(1.dp, Color.Black),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.holiday_target_img),
                    contentDescription = "user image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(999.dp))
                )
            }
            Text(
                text = "Никита",
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(0.3f),
                style = TextStyle(textAlign = TextAlign.Center)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(0.95f),
                colors = CardDefaults.cardColors(ProfileComponentsBg)
            ) {

                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Lv 17", style = TextStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight(900),
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace,
                            )
                        )

                        AnimatedDashedCard(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Text(
                                "До сдел.уровня 123 XP",
                                style = TextStyle(color = Color.DarkGray),
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { 0.6f },
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.LightGray),
                        color = MainBlue,
                        trackColor = Color.LightGray,
                        gapSize = 0.dp,
                        drawStopIndicator = { false },
                        strokeCap = StrokeCap.Round
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
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
                                " 110", style = TextStyle(
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
                                "111", style = TextStyle(
                                    color = Color.DarkGray,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(900),
                                    textAlign = TextAlign.Center
                                )
                            )
                        }

                    }

                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.fire_icon),
                    contentDescription = "streak",
                    tint = Orange
                )
                Text(text = " Серия 6 дней!")
            }

        }
    }


}

@Composable
fun AnimatedDashedCard(
    modifier: Modifier = Modifier,
    cornerRadius: Float = 30f,
    dashWidth: Float = 30f,
    dashGap: Float = 15f,
    strokeWidth: Float = 10f,
    borderColor: Color = MainBlue,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "dash")

    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = dashWidth + dashGap,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = LinearEasing
            )
        ),
        label = "phase"
    )

    Card(
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset.Zero,
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(dashWidth, dashGap),
                            phase = phase
                        )
                    )
                )
            },
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(ProfileComponentsBg)
    ) {
        content()
    }
}