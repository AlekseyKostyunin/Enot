package com.alekseykostyunin.enot.presentation.navigation

sealed class StartScreenState {
    data object NotAuthScreenState : StartScreenState()
    data object AuthScreenState : StartScreenState()
}