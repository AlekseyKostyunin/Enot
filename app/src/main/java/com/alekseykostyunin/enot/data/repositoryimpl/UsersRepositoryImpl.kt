package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.data.firebase.Firebase
import com.alekseykostyunin.enot.domain.repository.UsersRepository

object UsersRepositoryImpl : UsersRepository {

    override fun signInWithEmailAndPassword(email: String, password: String) {
        Firebase.signInWithEmailAndPassword(email,password)
    }

    override fun currentUser() : Boolean {
        return Firebase.currentUser()
    }

    override fun regUser(email: String, password: String) {
        Firebase.reg(email,password)
    }

    override fun resetPasswordUser(id: String) {
        Firebase.resetPassword(id)
    }

    override fun signOutUser() : Boolean {
        return Firebase.singOutUser()
    }
}