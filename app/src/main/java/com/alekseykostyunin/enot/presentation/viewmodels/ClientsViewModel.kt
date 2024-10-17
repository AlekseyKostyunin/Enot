package com.alekseykostyunin.enot.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.usecase.clients.AddClientUseCase
import com.alekseykostyunin.enot.domain.usecase.clients.AllClientsUseCase
import com.alekseykostyunin.enot.domain.usecase.clients.EditClientUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class ClientsViewModel(
    private val addClientUseCase: AddClientUseCase,
    private val allClientsUseCase: AllClientsUseCase,
    private val editClientUseCase: EditClientUseCase,
) : ViewModel() {

    var state: MutableStateFlow<State> = MutableStateFlow(State.Initial)
    var clients = MutableStateFlow<List<Client>>(listOf())
    var client = MutableStateFlow(Client())

    init {
        allClients()
    }

    fun resetState() {
        state.value = State.Initial
    }

    fun updateClients() {
        allClients()
    }

    private fun allClients() {
        allClientsUseCase.invoke()
            .onStart { state.value = State.Loading }
            .onEach { listClients ->
                clients.value = listClients
                state.value = State.Success
            }.catch {
                state.value = State.Error("Произошла ошибка. Попробуйте позже")
            }.launchIn(viewModelScope)
    }

    fun setClient(client: Client) {
        this.client.value = client
    }

    fun addClient(name: String, phone: List<String>) {
        val client = clients.value.find { it.name == name }
        if (client != null) {
            state.value = State.Error("Такой клиент уже есть")
            return
        } else {
            addClientUseCase.invoke(name, phone)
                .onStart { state.value = State.Loading }
                .onEach { newClient ->
                    setClient(newClient)
                    state.value = State.Success
                }.catch {
                    state.value = State.Error("Произошла ошибка. Попробуйте позже")
                }.launchIn(viewModelScope)
        }
    }

    fun editClient(name: String, phone: String) {
        val idClient = client.value.id
        if (idClient != null) {
            editClientUseCase.invoke(idClient, name, phone)
                .onStart { state.value = State.Loading }
                .onEach { newClient ->
                    setClient(newClient)
                    state.value = State.Success
                }.catch {
                    state.value = State.Error("Произошла ошибка. Попробуйте позже")
                }.launchIn(viewModelScope)
        }

    }

}