package com.alekseykostyunin.enot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.users.CurrentUserUseCase

class UsersViewModel: ViewModel() {

    private var _isUserAuth = MutableLiveData<Boolean>()
    var isUserAuth: LiveData<Boolean> = _isUserAuth

    fun isUserAuth() {
        val repository: UsersRepository = UsersRepositoryImpl
        val currentUserUseCase = CurrentUserUseCase(repository)
        _isUserAuth.value = currentUserUseCase.currentUser()
    }

}