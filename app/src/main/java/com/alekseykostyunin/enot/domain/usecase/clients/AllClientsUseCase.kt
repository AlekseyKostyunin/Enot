package com.alekseykostyunin.enot.domain.usecase.clients

import com.alekseykostyunin.enot.domain.repository.ClientsRepository

class AllClientsUseCase(private val clientsRepository: ClientsRepository) {
    fun getAllClients() = clientsRepository.getAllClients()
}