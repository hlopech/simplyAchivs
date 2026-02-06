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
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.R
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue

@Composable
fun GoalCard(onClick: () -> Unit) {

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(Color.White),
        modifier = Modifier.fillMaxWidth(1f).padding(10.dp),
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
                    onClick = {}, modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "open target detail info",
                        tint = LightGray,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.education_target_img),
                        contentDescription = "education target",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 10.dp)
                    )
                    Column() {

                        Text(
                            text = "Учебный марафон", style = TextStyle(
                                fontWeight = FontWeight(600), fontSize = 20.sp
                            ), modifier = Modifier.padding(top = 10.dp)
                        )
                        Text("")
                        LinearProgressIndicator(
                            progress = { 0.6f },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
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
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.complexity_icon),
                                tint = CoinColor,
                                contentDescription = "complexity",
                                modifier = Modifier.size(25.dp)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "+600 XP", style = TextStyle(
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
                                        contentDescription = "coins",
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text(
                                        "+50", style = TextStyle(
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