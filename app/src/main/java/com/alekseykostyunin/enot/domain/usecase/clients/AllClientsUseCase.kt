package com.alekseykostyunin.enot.domain.usecase.clients

import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow

class AllClientsUseCase(private val clientsRepository: ClientsRepository) {
    fun invoke(): Flow<List<Client>> = clientsRepository.getAllClients()
}