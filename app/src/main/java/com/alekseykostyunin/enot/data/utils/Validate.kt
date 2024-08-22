package com.alekseykostyunin.enot.data.utils

import android.text.TextUtils

class Validate {

    companion object{

        fun isEmailValid(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isNumericToX(toCheck: String): Boolean {
            return toCheck.toIntOrNull() != null
//            val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
//            return toCheck.matches(regex)
        }
    }
}