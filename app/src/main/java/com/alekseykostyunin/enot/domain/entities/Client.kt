package com.alekseykostyunin.enot.domain.entities

data class Client(
    val id: String? = null,
    val idOfContacts: String? = null,
    var name: String? = null,
    var phone: String? = null
)