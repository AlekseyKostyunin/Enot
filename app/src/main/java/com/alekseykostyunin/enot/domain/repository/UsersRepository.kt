package com.alekseykostyunin.enot.domain.repository

interface UsersRepository {
    fun authUser(email: String, password: String)
    fun currentUser() : Boolean
    fun regUser(email: String, password: String)
    fun getUser(id: String)
    fun getEmailUser(id: String)
    fun getIdUser()
    fun resetPasswordUser(id: String)
    fun signOutUser()

}