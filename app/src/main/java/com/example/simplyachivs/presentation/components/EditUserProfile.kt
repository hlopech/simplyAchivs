package com.example.simplyachivs.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

import com.example.simplyachivs.R
import com.example.simplyachivs.ui.theme.DarkGreen
import com.example.simplyachivs.ui.theme.LightGray
import com.example.simplyachivs.ui.theme.MainBlue

@Composable
fun EditUserProfile(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Uri?) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(currentName) }
    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri }
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать профиль") },
        text = {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    when (selectedImageUri) {
                        null -> {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            )
                        }

                        else -> {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(999.dp))
                            )
                        }

                    }
                    Button(
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                        onClick = { imagePicker.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            MainBlue
                        )
                    ) {
                        Text("Выбрать фото")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        if (it.length <= 30) {
                            name = it
                            nameError = null
                        }
                    },
                    label = { Text("Имя") },
                    isError = nameError != null,
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (nameError != null) {
                                Text(text = nameError!!, color = Color.Red, fontSize = 13.sp)
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            Text(
                                text = "${name.length}/30",
                                color = if (name.length >= 27) Color.Red else Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MainBlue,
                        unfocusedIndicatorColor = MainBlue,
                        errorIndicatorColor = Color.Red,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

            }
        },
        confirmButton = {
            TextButton(onClick = {
                val trimmed = name.trim()
                val error = when {
                    trimmed.isBlank() -> "Введите имя"
                    trimmed.length < 2 -> "Имя должно быть не менее 2 символов"
                    else -> null
                }
                if (error != null) {
                    nameError = error
                } else {
                    onConfirm(trimmed, selectedImageUri)
                }
            }) {
                Text("Сохранить", fontSize = 16.sp, color = MainBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", fontSize = 16.sp, color = Color.DarkGray)
            }
        }
    )
}

