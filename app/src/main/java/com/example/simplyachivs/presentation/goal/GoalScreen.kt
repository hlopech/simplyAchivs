package com.example.simplyachivs.presentation.goal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.R
import com.example.simplyachivs.presentation.components.MainButton
import com.example.simplyachivs.presentation.components.TargetCard
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue

@Composable
fun TargetScreen(onAddNewTarget: () -> Unit) {

    val isActiveSelectedOption = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Мои цели",
            style = TextStyle(
                fontWeight = FontWeight(900),
                fontSize = 25.sp
            ),
            modifier = Modifier
                .padding(10.dp)
                .padding(top = 30.dp)
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.95f),
        ) {
            MainButton(
                when (isActiveSelectedOption.value) {
                    true -> MainBlue
                    false -> LightGray
                },
                when (isActiveSelectedOption.value) {
                    true -> Color.White
                    false -> Color.Gray
                },
                {
                    isActiveSelectedOption.value = true
                },
                "Активные",
            )
            Spacer(modifier = Modifier.width(5.dp))

            MainButton(
                when (isActiveSelectedOption.value) {
                    false -> MainBlue
                    true -> LightGray
                },
                when (isActiveSelectedOption.value) {
                    false -> Color.White
                    true -> Color.Gray
                },
                {
                    isActiveSelectedOption.value = false
                },
                "Завершенные",
            )

        }
        when (isActiveSelectedOption.value) {
            true -> LazyColumn(
                modifier = Modifier.fillMaxWidth(0.95f),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(Color.White),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(10.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(5.dp),

                        ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.award_img),
                                contentDescription = "education target",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(end = 10.dp)
                            )
                            Column(modifier = Modifier.padding(bottom = 10.dp)) {

                                Text(
                                    text = "Создайте новую цель", style = TextStyle(
                                        fontWeight = FontWeight(600), fontSize = 20.sp
                                    ), modifier = Modifier.padding(top = 10.dp)
                                )

                                Text(
                                    text = "Поставьте амбициозную задачу!", style = TextStyle(
                                        fontWeight = FontWeight(600),
                                        fontSize = 15.sp,
                                        color = Color.Gray
                                    ), modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                                )

                                MainButton(
                                    MainBlue,
                                    Color.White,
                                    { onAddNewTarget() },
                                    "Новая цель",
                                    Icons.Default.Add,
                                )

                            }
                        }
                    }


                }
                items(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 10)) {
                    TargetCard({})
                }
            }

            false -> LazyColumn(
                modifier = Modifier.fillMaxWidth(0.95f),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 10)) {
                    TargetCard({})
                }
            }
        }


    }


}