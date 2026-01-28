package com.example.simplyachivs.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.packInts
import com.example.simplyachivs.R
import com.example.simplyachivs.ui.theme.MainBlue
import com.example.simplyachivs.ui.theme.ProfileComponentsBg

@Composable
fun ProfileOption(text: String, onClick: () -> Unit, icon: Int) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(1f).padding(vertical = 5.dp),
        colors = ButtonDefaults.buttonColors(ProfileComponentsBg),
        shape = RoundedCornerShape(5.dp),
        elevation = ButtonDefaults.buttonElevation(5.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(icon),
                    tint = MainBlue,
                    contentDescription = "option title",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 25.sp,
                        fontWeight = FontWeight(600)
                    ),
                    modifier = Modifier
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "option title",
                tint = MainBlue,
                modifier = Modifier.size(40.dp)


            )

        }
    }
}