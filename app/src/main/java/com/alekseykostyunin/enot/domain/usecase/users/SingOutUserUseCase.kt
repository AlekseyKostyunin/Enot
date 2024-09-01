package com.alekseykostyunin.enot.domain.usecase.users

import com.alekseykostyunin.enot.domain.repository.UsersRepository

class SingOutUserUseCase(
    private val usersRepository: UsersRepository
) {

    fun singOutUser() : Boolean{
        return usersRepository.signOutUser()
    }

}