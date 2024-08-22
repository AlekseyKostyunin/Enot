package com.alekseykostyunin.enot.domain.entities

data class HistoryStep(
    val id: Int,
    val time: String,
    val type: Int,
    val text: String,
    val foto: List<String?> = listOf("image_1", "image_2")
) {

}
