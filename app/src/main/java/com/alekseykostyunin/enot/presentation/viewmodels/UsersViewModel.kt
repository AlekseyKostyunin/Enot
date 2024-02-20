package com.alekseykostyunin.enot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.users.CurrentUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase

class UsersViewModel: ViewModel() {

    private var initial = isUserAuth() // не пустой!!!
    private var _isUserAuth = MutableLiveData<Boolean>(initial)
    var isUserAuth: LiveData<Boolean> = _isUserAuth

    private fun isUserAuth() : Boolean {
        val repository: UsersRepository = UsersRepositoryImpl
        val currentUserUseCase = CurrentUserUseCase(repository)
        return currentUserUseCase.currentUser()
    }

    fun signOutUser() {
        val repository: UsersRepository = UsersRepositoryImpl
        val singOutUserUseCase = SingOutUserUseCase(repository)
        initial = singOutUserUseCase.singOutUser()
    }



}