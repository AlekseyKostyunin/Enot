package com.alekseykostyunin.enot.domain.usecase.users

import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl

class SingOutUserUseCase(
    private val usersRepository: UsersRepositoryImpl
) {

    fun singOutUser() : Boolean{
        return usersRepository.signOutUser()
    }

}