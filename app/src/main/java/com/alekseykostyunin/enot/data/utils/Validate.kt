package com.alekseykostyunin.enot.data.utils

import android.text.TextUtils

class Validate {
    companion object{
        fun isEmailValid(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}