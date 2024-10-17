package com.alekseykostyunin.enot.domain.usecase.clients

import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.repository.ClientsRepository
import kotlinx.coroutines.flow.Flow

class AddClientUseCase(private val clientsRepository: ClientsRepository) {
    fun invoke(name: String, phone: List<String>): Flow<Client> = clientsRepository.addClient(name, phone)
}