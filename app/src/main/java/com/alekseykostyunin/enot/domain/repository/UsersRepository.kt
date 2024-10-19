package com.alekseykostyunin.enot.domain.repository

import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun auth(email: String, password: String, onResult: (Boolean) -> Unit): Flow<Boolean>
    fun regUser(email: String, password: String, onResult: (Boolean) -> Unit)
    fun resetPasswordUser(email: String, onResult: (Boolean) -> Unit)
    fun signOutUser()
}