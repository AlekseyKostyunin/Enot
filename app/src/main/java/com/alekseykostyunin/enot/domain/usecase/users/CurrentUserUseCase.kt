package com.alekseykostyunin.enot.domain.usecase.users

import com.alekseykostyunin.enot.domain.repository.UsersRepository

class CurrentUserUseCase(private val usersRepository: UsersRepository) {
    fun currentUser() : Boolean{
        return usersRepository.currentUser()
    }
}