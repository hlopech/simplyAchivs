package com.example.simplyachivs

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextRange

@Composable
fun DetailTaskScreen(taskId: String, onBack: () -> Unit) {
    Text(taskId)
    Button(onClick = onBack) {
        Text("Back")
    }
}