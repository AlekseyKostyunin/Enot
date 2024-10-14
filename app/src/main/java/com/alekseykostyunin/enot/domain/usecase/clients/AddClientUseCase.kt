package com.alekseykostyunin.enot.domain.usecase.clients

import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.repository.ClientsRepository

class AddClientUseCase(private val clientsRepository: ClientsRepository) {
    fun addClient(client: Client) = clientsRepository.addClient(client)
}