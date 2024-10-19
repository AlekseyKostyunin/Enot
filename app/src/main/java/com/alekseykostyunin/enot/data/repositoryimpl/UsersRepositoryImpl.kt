package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.data.firebase.AppFirebase
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow

object UsersRepositoryImpl : UsersRepository {

    override fun auth(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ): Flow<Boolean> {
        return AppFirebase.auth(email,password, onResult)
    }

    override fun regUser(email: String, password: String, onResult: (Boolean) -> Unit){
        AppFirebase.reg(email,password, onResult)
    }

    override fun resetPasswordUser(email: String, onResult: (Boolean) -> Unit) {
        AppFirebase.resetPassword(email, onResult)
    }

    override fun signOutUser() {
        AppFirebase.singOutUser()
    }
}