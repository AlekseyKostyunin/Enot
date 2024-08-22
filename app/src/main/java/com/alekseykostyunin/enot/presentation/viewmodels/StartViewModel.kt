package com.alekseykostyunin.enot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alekseykostyunin.enot.data.firebase.MyFirebaseAuth
import com.alekseykostyunin.enot.presentation.navigation.StartScreenState

class StartViewModel : ViewModel() {

    private val initialState = isUserAuth()
    private var _startScreenState = MutableLiveData<StartScreenState>(initialState)
    var startScreenState: LiveData<StartScreenState> = _startScreenState

    private fun isUserAuth() : StartScreenState {
        return if(MyFirebaseAuth.currentUser()) {
            StartScreenState.AuthScreenState
        } else StartScreenState.NotAuthScreenState
    }

    fun successAuth(){
        _startScreenState.value = StartScreenState.AuthScreenState
    }

    fun signOut(){
        _startScreenState.value = StartScreenState.NotAuthScreenState
    }

}