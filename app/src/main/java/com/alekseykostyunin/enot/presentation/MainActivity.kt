package com.alekseykostyunin.enot.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alekseykostyunin.enot.presentation.navigation.StartNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //EnotTheme {}
            StartNavigation()
        }
    }
}