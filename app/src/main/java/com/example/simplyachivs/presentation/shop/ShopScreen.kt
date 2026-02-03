package com.example.simplyachivs.presentation.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.R
import com.example.simplyachivs.presentation.components.AwardCard
import com.example.simplyachivs.ui.theme.MainBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(onAddAward: () -> Unit) {


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(expandedHeight = 50.dp, title = {
                Text("Магазин наград")
            }, navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.shop_icon),
                        contentDescription = "",
                        tint = MainBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }, actions = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.coin_img),
                        contentDescription = "coins",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        "34", modifier = Modifier.padding(5.dp), style = TextStyle(
                            color = Color.Black,
                            fontSize = 23.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center
                        )
                    )
                }
            })
        }) { paddingValues ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding())
//                .verticalScroll(rememberScrollState(), true)
        ) {
            Box(modifier = Modifier.padding(bottom = 100.dp)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                ) {
                    items(listOf(1, 2, 3, 4, 5, 6, 7)) {
                        AwardCard()
                    }
                }

                FloatingActionButton(
                    onClick = { onAddAward() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                    elevation = FloatingActionButtonDefaults.elevation(15.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        tint = MainBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

    }

}