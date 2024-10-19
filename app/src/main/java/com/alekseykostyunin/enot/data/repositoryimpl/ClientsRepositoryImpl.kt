package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.data.firebase.AppFirebase
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow

object ClientsRepositoryImpl: ClientsRepository {

    override fun getAllClients(): Flow<List<Client>> = AppFirebase.getAllClients()
    override fun addClient(name: String, phone: List<String>): Flow<Client> = AppFirebase.addClient(name, phone)
    override fun editClient(id: String, name: String, phone: String): Flow<Client> = AppFirebase.editClient(id, name, phone)
}