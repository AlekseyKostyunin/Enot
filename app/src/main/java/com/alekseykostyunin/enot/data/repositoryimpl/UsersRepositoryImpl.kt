package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.data.firebase.Firebase
import com.alekseykostyunin.enot.domain.repository.UsersRepository

object UsersRepositoryImpl : UsersRepository {

    override fun signInWithEmailAndPassword(email: String, password: String, onResult: (Boolean) -> Unit) {
        Firebase.signInWithEmailAndPassword(email,password, onResult)
    }

    override fun regUser(email: String, password: String, onResult: (Boolean) -> Unit){
        Firebase.reg(email,password, onResult)
    }

    override fun resetPasswordUser(email: String, onResult: (Boolean) -> Unit) {
        Firebase.resetPassword(email, onResult)
    }

    override fun signOutUser() {
        Firebase.singOutUser()
    }
}