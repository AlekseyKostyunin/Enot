package com.alekseykostyunin.enot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekseykostyunin.enot.data.firebase.AppFirebase
import com.alekseykostyunin.enot.domain.usecase.users.AuthUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.RegUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.ResetPasswordUseCase
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class UserViewModel(
    private val regUserUseCase: RegUserUseCase,
    private val authUserUseCase: AuthUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val singOutUserUseCase: SingOutUserUseCase,
) : ViewModel() {

    private var _isAuthorized = MutableStateFlow(AppFirebase.currentUser())
    val isAuthorized: StateFlow<Boolean> = _isAuthorized

    init {
        updateCurrentUser()
    }

    private fun updateCurrentUser(){
        _isAuthorized.value = AppFirebase.currentUser()
    }

    fun setStatusAuthorized(newStatus: Boolean) {
        _isAuthorized.value = newStatus
    }

    fun regUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        regUserUseCase.regUser(email, password, onResult)
    }

    fun resetPasswordUser(email: String, onResult: (Boolean) -> Unit) {
        resetPasswordUseCase.resetPasswordUser(email, onResult)
    }

    fun auth(email: String, password: String, onResult: (Boolean) -> Unit) {
        authUserUseCase.invoke(email, password, onResult)
            .onStart {
                Log.e("TEST_auth", "State.Loading")
            }.onEach { statusAuth ->
                updateCurrentUser()
                _isAuthorized.value = statusAuth
                Log.e("TEST_auth", "State.Success")
                Log.e("TEST_authSuc", statusAuth.toString())
            }.catch {
                Log.e("TEST_auth", it.message.toString())
            }.launchIn(viewModelScope)
    }

    fun signOut() {
        singOutUserUseCase.singOutUser()
        _isAuthorized.value = false
    }

}