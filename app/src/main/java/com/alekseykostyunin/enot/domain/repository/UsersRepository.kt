package com.alekseykostyunin.enot.domain.repository

interface UsersRepository {
    fun signInWithEmailAndPassword(email: String, password: String, onResult: (Boolean) -> Unit)
    fun regUser(email: String, password: String, onResult: (Boolean) -> Unit)
    fun resetPasswordUser(email: String, onResult: (Boolean) -> Unit)
    fun signOutUser()
}