package com.alekseykostyunin.enot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alekseykostyunin.enot.data.firebase.Firebase
import com.alekseykostyunin.enot.domain.usecase.users.AuthUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.CurrentUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.RegUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.ResetPasswordUseCase
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase

class UserViewModel(
    private val regUserUseCase: RegUserUseCase,
    private val authUserUseCase: AuthUserUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val singOutUserUseCase: SingOutUserUseCase,
) : ViewModel() {

    private val initialState = isStatusAuthorized()
    private val _isAuthorized = MutableLiveData(initialState)

    val isAuthorized: LiveData<Boolean> = _isAuthorized
    private fun isStatusAuthorized(): Boolean = Firebase.currentUser()

    fun signInWithEmailAndPassword(email: String, password: String) {
        regUserUseCase.regUser(email, password)
    }

    fun successAuth() {
        _isAuthorized.value = true
    }

    fun signOut() {
        singOutUserUseCase.singOutUser()
        _isAuthorized.value = false
    }


}