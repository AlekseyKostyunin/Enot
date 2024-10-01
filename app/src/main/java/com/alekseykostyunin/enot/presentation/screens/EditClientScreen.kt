package com.alekseykostyunin.enot.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun EditClientScreen(
    navigationState: NavigationState,
    clientsViewModel: ClientsViewModel
) {
    val client = clientsViewModel.client.observeAsState().value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navigationState.navigateTo(Destinations.OneClientAllOrders.route)
                }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(R.string.edit_client),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            var nameClient by remember { mutableStateOf(client?.name ?: "") }
            val isErrorNameClient by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorNameClient,
                modifier = Modifier.fillMaxWidth(),
                value = nameClient,
                label = { Text(stringResource(R.string.name_client)) },
                onValueChange = { newText -> nameClient = newText }
            )

            var phoneClient by remember { mutableStateOf(client?.phone?.joinToString(", ") ?: "") }
            val isErrorPhoneClient by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorPhoneClient,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = phoneClient,
                label = { Text(stringResource(R.string.phones_client)) },
                onValueChange = { newText -> phoneClient = newText }
            )

            Text(stringResource(R.string.phone_separated), Modifier.padding(vertical = 10.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = {

                    val auth: FirebaseAuth = Firebase.auth
                    val database = Firebase.database.reference
                    val user = auth.currentUser

                    if (user != null) {
                        val userId = user.uid
                        val idClient = client?.id
                        idClient?.let {
                            val clientUpdate = Client(
                                id = idClient,
                                name = nameClient,
                                phone = phoneClient.split(", ")
                            )

                            database
                                .child("users")
                                .child(userId)
                                .child("clients")
                                .child(idClient)
                                .setValue(clientUpdate)

                            val dbNewClientUpdate = database
                                .child("users")
                                .child(userId)
                                .child("orders")
                                .child(idClient)

                            dbNewClientUpdate.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val client2 = snapshot.getValue(Client::class.java)
                                    if (client2 != null) {
                                        Log.d("TEST_snapshot_EditOrderScreen", client2.toString())
                                        client2.id?.let { it1 -> clientsViewModel.loadClient(it1) }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("TEST_snapshot_error", error.message)
                                }
                            })
                            clientsViewModel.updateClients()
                            navigationState.navigateTo(Destinations.OneClientAllOrders.route)
                        }

                    }
                }
            ) {
                Text(text = stringResource(R.string.save_changes))
            }
        }

    }

}