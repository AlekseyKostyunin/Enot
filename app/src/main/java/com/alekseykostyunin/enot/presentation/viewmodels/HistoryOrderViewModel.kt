package com.alekseykostyunin.enot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryOrderViewModel : ViewModel() {

    private var _history = MutableLiveData<List<String>>(listOf())
    var history: LiveData<List<String>> = _history

    fun updateHistory(){
        //return
    }
}