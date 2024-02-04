package com.alekseykostyunin.enot.domain.usecase.clients

import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.repository.ClientsRepository

class AddNewClientUseCase(private val clientsRepository: ClientsRepository) {
    fun addNewClient(client: Client){
        clientsRepository.addClient(client)
    }
}