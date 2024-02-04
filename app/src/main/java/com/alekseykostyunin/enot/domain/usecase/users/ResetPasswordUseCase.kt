package com.alekseykostyunin.enot.domain.usecase.users

import com.alekseykostyunin.enot.domain.repository.UsersRepository

class ResetPasswordUseCase(private val usersRepository: UsersRepository) {
    fun resetPasswordUser(idUser:String){
        usersRepository.resetPasswordUser(idUser)
    }
}