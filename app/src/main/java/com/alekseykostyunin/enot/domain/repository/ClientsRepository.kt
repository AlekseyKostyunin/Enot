package com.alekseykostyunin.enot.domain.repository

import com.alekseykostyunin.enot.domain.entities.Client
import kotlinx.coroutines.flow.Flow

interface ClientsRepository {
    fun addClient(name: String, phone: List<String>): Flow<Client>
    fun getAllClients(): Flow<List<Client>>
    fun editClient(id: String, name: String, phone: String): Flow<Client>
}