package com.example.simplyachivs.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.simplyachivs.R

@Composable
fun ProfileOptions(options: Any) {

    LazyColumn(contentPadding = PaddingValues(vertical = 10.dp), modifier = Modifier.padding(vertical = 0.dp)) {
        items(options as List<*>) { it ->
            ProfileOption("Опция ${it}", {}, R.drawable.brush_icon)
        }
    }


}