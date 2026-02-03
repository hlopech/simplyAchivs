package com.example.simplyachivs.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.example.simplyachivs.presentation.navigation.AppNavHost
import com.example.simplyachivs.presentation.navigation.BottomNavigationBar
import com.example.simplyachivs.presentation.navigation.rememberAppState

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            Scaffold(bottomBar = {
                BottomNavigationBar(appState)
            }) {
                AppNavHost(appState)
            }
        }
    }
}