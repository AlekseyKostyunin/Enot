package com.alekseykostyunin.enot.domain.entities

data class Order(
    val id: String? = null,
    val client: String? = null,
    val dateAdd: String? = null,
    var dateClose: String? = null,
    var description: String? = null,
    var type: String? = null,
    var model: String? = null,
    var priceZip: Int = 0,
    var priceWork: Int = 0,
    var isWork: Boolean = true,
    var history: List<HistoryStep>? = null,
    var photos: List<Photo>? = null,
    var comment: String? = null
)