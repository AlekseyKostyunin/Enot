package com.alekseykostyunin.enot.data.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class DateUtil {

    companion object {
        val dateOfUnit = System.currentTimeMillis()

        @SuppressLint("SimpleDateFormat")
        fun dateFormatterHHmm(milliseconds: String): String {
            return SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(milliseconds.toLong()))
                .toString()
        }

        @SuppressLint("SimpleDateFormat")
        fun dateFormatter(milliseconds: String): String {
            return SimpleDateFormat("dd.MM.yyyy").format(Date(milliseconds.toLong())).toString()
        }

        @SuppressLint("SimpleDateFormat")
        fun dateFormatterHHmm(milliseconds: Long): String {
            return SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(milliseconds)).toString()
        }

        @SuppressLint("SimpleDateFormat")
        fun dateFormatter(milliseconds: Long): String {
            return SimpleDateFormat("dd.MM.yyyy").format(Date(milliseconds)).toString()
        }
    }
}