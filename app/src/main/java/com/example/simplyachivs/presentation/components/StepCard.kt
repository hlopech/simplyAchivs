package com.example.simplyachivs.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.R
import com.example.simplyachivs.domain.model.goal.Step
import java.util.UUID

@Composable
fun StepCard(
    step: Step, onDeleteStep: () -> Unit, modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(0.95f)
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))

    ) {
        Text(
            "${step.position} шаг", style = TextStyle(
                fontWeight = FontWeight(900),
                fontSize = 18.sp,
                color = Color.Black
            ), modifier = Modifier.padding(10.dp)
        )
        VerticalDivider(modifier = Modifier.height(70.dp))

        TextField(
            value = step.name,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { onDeleteStep() }) {
                    Icon(
                        painter = painterResource(R.drawable.delete_icon),
                        contentDescription = "delete step",
                        tint = Color.Red,
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            onValueChange = { },
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
}