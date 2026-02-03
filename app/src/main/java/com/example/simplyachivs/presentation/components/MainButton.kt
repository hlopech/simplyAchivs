package com.example.simplyachivs.presentation.components

import android.graphics.drawable.Icon
import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplyachivs.ui.theme.MainBlue

@Composable
fun MainButton(
    bgColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    text: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(bgColor),
        elevation = ButtonDefaults.buttonElevation(10.dp),
        shape = RoundedCornerShape(15.dp)
    ) {

        Card(
            colors = CardDefaults.cardColors(Color.Transparent),
            modifier = Modifier.wrapContentSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        contentDescription = "addIcon",
                        modifier = Modifier.size(20.dp),
                        tint = contentColor
                    )
                }
                Text(
                    text = text, style = TextStyle(
                        color = contentColor,

                        )
                )
            }
        }
    }

}