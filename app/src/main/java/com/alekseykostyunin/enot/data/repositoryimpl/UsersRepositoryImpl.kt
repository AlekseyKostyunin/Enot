package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.data.firebase.MyFirebaseAuth
import com.alekseykostyunin.enot.domain.repository.UsersRepository

object UsersRepositoryImpl : UsersRepository {


    override fun authUser(email: String, password: String) {
        MyFirebaseAuth.auth(email,password)
    }

    override fun currentUser() : Boolean {
        return MyFirebaseAuth.currentUser()
    }

    override fun regUser(email: String, password: String) {
        MyFirebaseAuth.reg(email,password)
    }

    override fun getUser(id: String) {
        TODO("Not yet implemented")
    }

    override fun getEmailUser(id: String) {
        TODO("Not yet implemented")
    }

    override fun getIdUser() {
        TODO("Not yet implemented")
    }

    override fun resetPasswordUser(email: String) {
        MyFirebaseAuth.resetPassword(email)
    }

    override fun signOutUser() {
        MyFirebaseAuth.singOutUser()
    }
}