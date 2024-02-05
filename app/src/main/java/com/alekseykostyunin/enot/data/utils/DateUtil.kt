package com.alekseykostyunin.enot.data.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class DateUtil {

    companion object{
        val dateOfUnit = System.currentTimeMillis().toString()

        @SuppressLint("SimpleDateFormat")
        fun dateFormatter(milliseconds: String): String {
            return SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date(milliseconds.toLong())).toString()
        }
    }
}