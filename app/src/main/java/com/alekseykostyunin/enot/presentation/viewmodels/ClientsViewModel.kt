package com.alekseykostyunin.enot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.usecase.clients.AddClientUseCase
import com.alekseykostyunin.enot.domain.usecase.clients.AllClientsUseCase
import com.alekseykostyunin.enot.domain.usecase.clients.EditClientUseCase
import com.alekseykostyunin.enot.presentation.navigation.State
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
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
        loadAllClients()
        allClients()
    }

    private fun allClients() {
        allClientsUseCase.invoke()
            .onStart {
                state.value = State.Loading
                Log.e("TEST_allClients", "State.Loading")
            }
            .onEach { listClients ->
                clients.value = listClients.sortedBy { it.name }
                state.value = State.Success
            }.catch {
                state.value = State.Error("Произошла ошибка. Попробуйте позже")
                Log.e("TEST_allClients", it.message.toString())
            }.launchIn(viewModelScope)
    }

    private fun loadAllClients() {
        state.value = State.Loading
        val auth: FirebaseAuth = Firebase.auth
        val database = Firebase.database.reference
        val user = auth.currentUser
        val clientsDB = mutableListOf<Client>()
        if (user != null) {
            val userId = user.uid
            val db = database.child("users").child(userId).child("clients")
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val client = i.getValue(Client::class.java)
                        if (client != null) {
                            clientsDB.add(client)
                        }
                    }
                    clients.value = clientsDB.sortedBy { it.name }
                    state.value = State.Success
                    Log.d("TEST_snapshot_clientsDB", clients.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            })
        }
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