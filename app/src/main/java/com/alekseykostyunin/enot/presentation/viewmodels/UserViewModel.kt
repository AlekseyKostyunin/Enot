package com.alekseykostyunin.enot.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.alekseykostyunin.enot.data.firebase.Firebase
import com.alekseykostyunin.enot.domain.usecase.users.AuthUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.RegUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.ResetPasswordUseCase
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class UserViewModel(
    private val regUserUseCase: RegUserUseCase,
    private val authUserUseCase: AuthUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val singOutUserUseCase: SingOutUserUseCase,
) : ViewModel() {

    private val initialState = Firebase.currentUser()
    val isAuthorized = MutableStateFlow(initialState)

    fun regUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        regUserUseCase.regUser(email, password, onResult)
    }

    fun resetPasswordUser(email: String, onResult: (Boolean) -> Unit) {
        resetPasswordUseCase.resetPasswordUser(email, onResult)
    }

    fun auth(email: String, password: String, onResult: (Boolean) -> Unit) {
         authUserUseCase.authUser(email, password, onResult)
        isAuthorized.value = true
    }

    fun signOut() {
        singOutUserUseCase.singOutUser()
        isAuthorized.value = false
    }

}