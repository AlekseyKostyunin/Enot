package com.alekseykostyunin.enot.presentation.viewmodelsfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alekseykostyunin.enot.domain.usecase.clients.AddClientUseCase
import com.alekseykostyunin.enot.domain.usecase.clients.AllClientsUseCase
import com.alekseykostyunin.enot.domain.usecase.clients.EditClientUseCase
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel

class ClientsViewModelFactory(
    private val addClientUseCase: AddClientUseCase,
    private val allClientsUseCase: AllClientsUseCase,
    private val editClientUseCase: EditClientUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClientsViewModel(
                addClientUseCase = addClientUseCase,
                allClientsUseCase = allClientsUseCase,
                editClientUseCase = editClientUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}