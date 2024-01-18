package com.alekseykostyunin.enot.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun Dfdfljs(){
    Text(
        text = "Click Me",
        style = TextStyle(color = Color.White),
        modifier = Modifier
            .clickable(onClick = {})
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Blue,
                        Color.Green
                    )
                ),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}