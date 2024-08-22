package com.alekseykostyunin.enot.presentation.viewmodels

sealed class State {
    data object Initial : State()
    data object Loading : State()
    data class Error(val textError: String = "Ошибка") : State()
    data object Success : State()
}