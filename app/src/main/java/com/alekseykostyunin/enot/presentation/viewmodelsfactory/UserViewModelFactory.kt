package com.alekseykostyunin.enot.presentation.viewmodelsfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alekseykostyunin.enot.domain.usecase.users.AuthUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.CurrentUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.RegUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.ResetPasswordUseCase
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase
import com.alekseykostyunin.enot.presentation.viewmodels.UserViewModel

class UserViewModelFactory(
    private val regUserUseCase: RegUserUseCase,
    private val authUserUseCase: AuthUserUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val singOutUserUseCase: SingOutUserUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(
                regUserUseCase = regUserUseCase,
                authUserUseCase = authUserUseCase,
                currentUserUseCase = currentUserUseCase,
                resetPasswordUseCase = resetPasswordUseCase,
                singOutUserUseCase = singOutUserUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}