package com.example.simplyachivs.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.R
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue

@Composable
fun AwardCard() {
    Button(
        onClick = {},
        elevation = ButtonDefaults.buttonElevation(10.dp),
        colors = ButtonDefaults.buttonColors(MainBlue),
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(25.dp),
        contentPadding = PaddingValues(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource((R.drawable.award_img)),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )

            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                Text(
                    text = "Учебный марафон", style = TextStyle(
                        fontWeight = FontWeight(600), fontSize = 20.sp
                    ), modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = "Поставьте амбициозную задачу!", style = TextStyle(
                        fontWeight = FontWeight(600),
                        fontSize = 15.sp,
                        color = Color.White
                    ), modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.coin_img),
                    contentDescription = "coins",
                    modifier = Modifier.size(30.dp)
                )

                Text(
                    "1233", style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        textAlign = TextAlign.Center
                    )
                )
            }

        }
    }
}