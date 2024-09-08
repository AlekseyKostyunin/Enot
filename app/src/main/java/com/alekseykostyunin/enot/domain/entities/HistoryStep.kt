package com.alekseykostyunin.enot.domain.entities

data class HistoryStep(
    val id: Int = 0,
    val time: Long = 0,
    var type: Int = 0,
    val text: String? = null
)