package com.alekseykostyunin.enot.presentation.general

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.alekseykostyunin.enot.R

@Composable
fun ProgressIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.width(64.dp))
    }
}

@Composable
fun ProgressIndicatorLogo() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoAnimation()
    }
}

@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun Circle(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.composed {
            size(12.dp)
                .clip(CircleShape)
                .background(color)
        }
    )
}

@Preview
@Composable
fun LogoAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.enot)
    )
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}