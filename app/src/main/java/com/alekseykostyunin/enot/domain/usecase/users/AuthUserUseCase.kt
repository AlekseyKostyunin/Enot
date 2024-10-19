package com.alekseykostyunin.enot.domain.usecase.users

import com.alekseykostyunin.enot.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow

class AuthUserUseCase(private val usersRepository: UsersRepository) {
    fun invoke(email: String, password: String, onResult: (Boolean) -> Unit): Flow<Boolean> =
        usersRepository.auth(email, password, onResult)
}