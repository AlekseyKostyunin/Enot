package com.alekseykostyunin.enot.domain.entities

data class HistoryStep(
    val id: Int = 0,
    val time: String? = null,
    var type: Int = 0,
    val text: String? = null
)