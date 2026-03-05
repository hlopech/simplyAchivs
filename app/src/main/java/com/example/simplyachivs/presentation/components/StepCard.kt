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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplyachivs.R
import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue

@Composable
fun StepCard(
    step: Step, onDeleteStep: () -> Unit, modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(0.95f)
            .background(Color.White)
            .border(1.dp, LightGray, RoundedCornerShape(12.dp))
    ) {
        Text(
            "${step.position}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MainBlue,
            modifier = Modifier.padding(horizontal = 14.dp)
        )
        VerticalDivider(modifier = Modifier.height(56.dp))
        Text(
            text = step.name,
            fontSize = 15.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        )
        IconButton(onClick = onDeleteStep) {
            Icon(
                painter = painterResource(R.drawable.delete_icon),
                contentDescription = "delete step",
                tint = Color.Red,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
