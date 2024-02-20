package com.alekseykostyunin.enot.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.alekseykostyunin.enot.presentation.navigation.StartNavigation
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.StartViewModel
import com.alekseykostyunin.enot.ui.theme.EnotTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //EnotTheme {           }
            StartNavigation()
        }
    }
}