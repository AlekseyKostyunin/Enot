package com.alekseykostyunin.enot.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object MyFirebaseAuth {

    private var auth: FirebaseAuth = Firebase.auth

    fun currentUser(): Boolean {
        val user: FirebaseUser? = auth.currentUser
        Log.d("TEST_currentUser", user.toString())
        return if (user == null) {
            Log.d("TEST_currentUser", "not")
            false
        } else {
            Log.d("TEST_currentUser", user.uid)
            true
        }
    }

//    fun currentUser2() : Boolean {
//        val user: FirebaseUser? = auth.currentUser
//
//        auth.addAuthStateListener { // Слушатель состояния авторизации
//            if (user == null) {
//                Log.d("TEST_currentUser", it.toString())
//
//            } else {
//                Log.d("TEST_currentUser", user.uid)
//
//            }
//        }
//        return true
//    }

    fun auth(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("TEST_sign", "signInWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    Log.w("TEST_sign", "signInWithEmail:failure", task.exception)
                    //updateUI(null)
                }
            }
            .addOnFailureListener {
                Log.w("TEST_", it.toString())
            }
    }

    fun singOutUser(): Boolean {
        auth.signOut()
        Log.d("TEST_singOutUser", "ok")
        return true
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d("TEST_1", "yes" + it.toString())
            }.addOnFailureListener {
                Log.d("TEST_1", "not" + it.message)
            }
    }

    fun reg(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("TEST_1", "createUserWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    Log.w("TEST_1", "createUserWithEmail:failure", task.exception)
                    //updateUI(null)
                }
            }
    }

}