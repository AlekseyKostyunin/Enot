package com.alekseykostyunin.enot.domain.entities

data class Order(
    val id: Int? = null,
    val dateAdd: String? = null,
    val dateClose: String? = null,
    val clientId: String? = null,
    var description: String? = null,
    var type: String? = null,
    var model: String? = null,
    var priceZip: Int? = 0,
    var priceWork: Int? = 0
)