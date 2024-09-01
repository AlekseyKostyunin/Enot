package com.alekseykostyunin.enot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alekseykostyunin.enot.data.firebase.MyFirebaseAuth
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase

class MainViewModel : ViewModel() {

    private val initialState = isStatusAuthorized()
    private val _isAuthorized = MutableLiveData<Boolean>(initialState)

    val isAuthorized: LiveData<Boolean> = _isAuthorized
    private fun isStatusAuthorized() : Boolean = MyFirebaseAuth.currentUser()

    fun successAuth(){ _isAuthorized.value = true  }

    fun signOut() {
        val repository: UsersRepository = UsersRepositoryImpl
        val singOutUserUseCase = SingOutUserUseCase(repository)
        singOutUserUseCase.singOutUser()
        _isAuthorized.value = false
    }
}