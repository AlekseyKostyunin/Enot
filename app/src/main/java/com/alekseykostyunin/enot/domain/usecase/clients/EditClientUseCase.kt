package com.alekseykostyunin.enot.domain.usecase.clients

import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.repository.ClientsRepository

class EditClientUseCase(private val clientsRepository: ClientsRepository) {
    fun editClient(client: Client) = clientsRepository.editClient(client)
}