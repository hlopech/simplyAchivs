package com.example.simplyachivs.presentation.goal.addGoal

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.R
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.MainBlueLight
import com.example.simplyachivs.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTargetScreen(onBack: () -> Unit, onAddTarget: () -> Unit) {

    val targetName = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(expandedHeight = 50.dp, title = {
                Text("Новая цель")
            }, navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = MainBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }, actions = {
                IconButton(onClick = { onAddTarget() }) {
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding(), bottom = 100.dp)
                .verticalScroll(rememberScrollState(), true)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.awards_image),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(150.dp)
                        .width(300.dp)
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(20.dp)),

                    )

                Button(
                    onClick = {}, colors = ButtonDefaults.buttonColors(MainBlueLight)
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
                    value = targetName.value,
                    onValueChange = { targetName.value = it },
                    placeholder = {
                        Text(
                            "Введите название...", style = TextStyle(
                                fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Gray
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
                    value = targetName.value,
                    onValueChange = { targetName.value = it },
                    placeholder = {
                        Text(
                            "Добавьте описание (необязательно)", style = TextStyle(
                                fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Gray
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
                            "1 шаг", style = TextStyle(
                                fontWeight = FontWeight(900),
                                fontSize = 18.sp,
                                color = Color.Black
                            ), modifier = Modifier.padding(10.dp)
                        )
                        VerticalDivider(modifier = Modifier.height(70.dp))

                        TextField(
                            value = targetName.value,
                            trailingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        painter = painterResource(R.drawable.delete_icon),
                                        contentDescription = "delete step",
                                        tint = Color.Red,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            },
                            onValueChange = { targetName.value = it },
                            placeholder = {
                                Text(
                                    "Введите подцель...",
                                    style = TextStyle(
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

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(MainBlueLight)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add step",
                                tint = MainBlue,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Добавить шаг", style = TextStyle(
                                    fontWeight = FontWeight(500), fontSize = 18.sp, color = MainBlue
                                )
                            )

                        }
                    }



                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.Absolute.SpaceAround

                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = {
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.complexity_icon),
                                    contentDescription = "",
                                    tint = DarkGreen, modifier = Modifier.size(80.dp)
                                )
                            }
                            Text(
                                text = "Легкая",
                                style = TextStyle(
                                    fontWeight = FontWeight(900),
                                    fontSize = 16.sp,
                                    color = DarkGreen
                                )
                            )

                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = {
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.complexity_icon),
                                    contentDescription = "",
                                    tint = CoinColor, modifier = Modifier.size(80.dp)
                                )
                            }
                            Text(
                                text = "Средняя",
                                style = TextStyle(
                                    fontWeight = FontWeight(900),
                                    fontSize = 16.sp,
                                    color = CoinColor
                                )
                            )

                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = {
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.complexity_icon),
                                    contentDescription = "",
                                    tint = Color.Red, modifier = Modifier.size(80.dp)
                                )
                            }
                            Text(
                                text = "Сложная",
                                style = TextStyle(
                                    fontWeight = FontWeight(900),
                                    fontSize = 16.sp,
                                    color = Color.Red
                                )
                            )

                        }
                    }






                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
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
                        modifier = Modifier.fillMaxWidth(0.5f)
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
                                " 15", style = TextStyle(
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
                                " +120",
                                style = TextStyle(
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