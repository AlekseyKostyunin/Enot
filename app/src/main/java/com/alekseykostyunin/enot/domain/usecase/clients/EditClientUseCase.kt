package com.alekseykostyunin.enot.domain.usecase.clients

import com.alekseykostyunin.enot.domain.repository.ClientsRepository

class EditClientUseCase(private val clientsRepository: ClientsRepository) {
    fun invoke(id: String, name: String, phone: String) = clientsRepository.editClient(id, name, phone)
}