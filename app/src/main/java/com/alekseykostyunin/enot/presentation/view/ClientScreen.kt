package com.alekseykostyunin.enot.presentation.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

/* Клиенты  *********************************************/


@Composable
fun Clients(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(
                text = "Клиенты",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { }
                ) {
                    Text(text = "Последние")
                }
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { },

                    ) {
                    Text(text = "По алфавиту")
                }
            }
            for (i in 0 until 15) {
                GetOneClient(number = i)
            }
        }

    }

}


@Composable
fun GetOneClient(number: Int) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(0.1.dp, Color.Black),
        modifier = Modifier
            //.size(width = 340.dp, height = 100.dp)
            .fillMaxSize()
            .padding(vertical = 10.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Фамилия Имя Отчество № $number",

            )
    }
}