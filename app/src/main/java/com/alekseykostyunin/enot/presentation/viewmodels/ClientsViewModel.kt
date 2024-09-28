package com.alekseykostyunin.enot.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alekseykostyunin.enot.data.repositoryimpl.ClientsRepositoryImpl
import com.alekseykostyunin.enot.domain.entities.Client
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ClientsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ClientsRepositoryImpl

    private var _state = MutableLiveData<State>(State.Initial)
    var state: LiveData<State> = _state

    private var _clients = MutableLiveData<List<Client>>(listOf())
    var clients: LiveData<List<Client>> = _clients

    private var _client = MutableLiveData<Client>(Client())
    var client: LiveData<Client> = _client

    init {
        loadAllClients()
    }

    fun resetState() {
        _state.value = State.Initial
    }

    fun updateClients() {
        loadAllClients()
    }

    private fun loadAllClients() {
        _state.value = State.Loading
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
                    _clients.value = clientsDB
                    _state.value = State.Success
                    Log.d("TEST_snapshot_clientsDB", _clients.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            })
        }
    }

    fun loadClient(id: String) {
        _client.value = _clients.value?.find { it.id == id }
    }

    fun addClient(name: String, phone: List<String>) {
        val auth: FirebaseAuth = Firebase.auth
        val database = Firebase.database.reference
        val user = auth.currentUser
        if (user != null) {
            // Проверяем, есть ли такой клиент в базе
            val client = _clients.value?.find { it.name == name }
            if (client != null) {
                _state.value = State.Error("Такой клиент уже есть")
                return
            }

            val userId = user.uid
            val idClient = database
                .child("users")
                .child(userId)
                .child("clients")
                .push().key.toString()
            val clientNew = Client(idClient, name, phone)
            database
                .child("users")
                .child(userId)
                .child("clients")
                .child(idClient)
                .setValue(clientNew)
            updateClients()
        }
    }
}