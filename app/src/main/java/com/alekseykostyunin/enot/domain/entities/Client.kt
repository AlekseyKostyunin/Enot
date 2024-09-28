package com.alekseykostyunin.enot.domain.entities

data class Client(
    val id: String? = null,
    var name: String? = null,
    var phone: List<String>? = null
)