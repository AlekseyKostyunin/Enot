package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.data.firebase.Firebase
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow

object ClientsRepositoryImpl: ClientsRepository {

    override fun getAllClients(): Flow<List<Client>> = Firebase.getAllClients()
    override fun addClient(name: String, phone: List<String>): Flow<Client> = Firebase.addClient(name, phone)
    override fun editClient(id: String, name: String, phone: String): Flow<Client> = Firebase.editClient(id, name, phone)
}