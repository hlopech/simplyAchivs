package com.example.simplyachivs.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.simplyachivs.R
import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue

@Composable
fun GoalCard(goal: Goal, onClick: () -> Unit) {

    val complexityColor = when (goal.complexity) {
        GoalComplexity.EASY -> DarkGreen
        GoalComplexity.MEDIUM -> CoinColor
        GoalComplexity.HARD -> Color.Red
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(5.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Открыть цель",
                        tint = LightGray,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (goal.image != null) {
                        Image(
                            painter = rememberAsyncImagePainter(goal.image),
                            contentDescription = goal.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 10.dp)
                                .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.education_target_img),
                            contentDescription = goal.name,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 10.dp)
                        )
                    }

                    Column {
                        Text(
                            text = goal.name,
                            style = TextStyle(fontWeight = FontWeight(600), fontSize = 18.sp),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(0.85f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (goal.description.isNotBlank()) {
                            Text(
                                text = goal.description,
                                style = TextStyle(fontSize = 13.sp, color = Color.Gray),
                                modifier = Modifier.padding(top = 2.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(top = 10.dp, bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.complexity_icon),
                                tint = complexityColor,
                                contentDescription = "сложность",
                                modifier = Modifier.size(25.dp)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "+${goal.complexity.xp} XP",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(900),
                                        textAlign = TextAlign.Center
                                    )
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(R.drawable.coin_img),
                                        contentDescription = "монеты",
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text(
                                        "+${goal.complexity.coins}",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 15.sp,
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
    }
}
