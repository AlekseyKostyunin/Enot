package com.alekseykostyunin.enot.tmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MyComposableFunction() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Red,
            thickness = 3.dp
        )
    }
}

//Modifier.background(Color.White)