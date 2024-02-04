package com.alekseykostyunin.enot.domain.repository

import com.alekseykostyunin.enot.domain.entities.Client

interface ClientsRepository {
    fun addClient(client: Client)
    fun getAllClients()
    fun getInfoClient(idClient: String)
}