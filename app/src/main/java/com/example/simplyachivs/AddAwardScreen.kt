package com.example.simplyachivs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.components.MainButton
import com.example.simplyachivs.ui.theme.CoinColor
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.MainBlueLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAwardScreen(onBack:()->Unit,onAddAward:()->Unit) {

    val awardName = remember { mutableStateOf("") }

    val min = 1
    val max = 9999

    var value by remember { mutableIntStateOf(1) }
    var text by remember { mutableStateOf(value.toString()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(expandedHeight = 50.dp, title = {
                Text("Новая награда")
            }, navigationIcon = {
                IconButton(onClick = { onBack()}) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = MainBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }, actions = {
                IconButton(onClick = { onAddAward()}) {
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
                        "Название награды", style = TextStyle(
                            fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Black
                        )
                    )
                }
                OutlinedTextField(
                    value = awardName.value,
                    onValueChange = { awardName.value = it },
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
                    value = awardName.value,
                    onValueChange = { awardName.value = it },
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.coin_img),
                        contentDescription = "target description input",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        "Укажите стоимость ", style = TextStyle(
                            fontWeight = FontWeight(400), fontSize = 18.sp, color = Color.Black
                        )
                    )
                }




                Column(modifier = Modifier) {
                    Slider(
                        value = value.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = MainBlue,
                            activeTrackColor = MainBlue,
                            activeTickColor = MainBlue,

                            ),
                        onValueChange = { v ->
                            value = v.toInt().coerceIn(min, max)
                            text = value.toString()
                        },
                        valueRange = min.toFloat()..max.toFloat(),
                        // steps можно не задавать — будет плавный, но мы всё равно округляем toInt()
                    )



                    OutlinedTextField(
                        value = text,
                        onValueChange = { new ->
                            text = new.filter { it.isDigit() }.take(4) // 1..9999
                            val parsed = text.toIntOrNull()
                            if (parsed != null) value = parsed.coerceIn(min, max)
                        }, placeholder = {
                            Text(
                                "Введите название...", style = TextStyle(
                                    fontWeight = FontWeight(400),
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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


            }

            MainButton(
                MainBlue,
                Color.White,
                {onAddAward() },
                "Добавить награду",
                Icons.Default.Add,
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}