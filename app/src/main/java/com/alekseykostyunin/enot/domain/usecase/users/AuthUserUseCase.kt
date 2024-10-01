package com.alekseykostyunin.enot.domain.usecase.users

import com.alekseykostyunin.enot.domain.repository.UsersRepository

class AuthUserUseCase(private val usersRepository: UsersRepository) {
    fun authUser(email: String, password: String){
        usersRepository.signInWithEmailAndPassword(email, password)
    }
}