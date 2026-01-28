package com.example.simplyachivs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.example.simplyachivs.navigation.AppNavHost
import com.example.simplyachivs.navigation.BottomNavigationBar
import com.example.simplyachivs.navigation.rememberAppState


class MainActivity : ComponentActivity() {
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


