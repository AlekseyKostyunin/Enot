package com.alekseykostyunin.enot.domain.usecase.users

import com.alekseykostyunin.enot.domain.repository.UsersRepository

class RegUserUseCase(private val usersRepository: UsersRepository) {
    fun regUser(email: String, password: String, onResult: (Boolean) -> Unit)=
        usersRepository.regUser(email, password, onResult)
}