package com.alekseykostyunin.enot.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.State
import com.alekseykostyunin.enot.ui.theme.Purple40

@Composable
fun AllClientsScreen(
    navigationState: NavigationState,
    clientsViewModel: ClientsViewModel,
    getContact: () -> Unit,
    requestContactsPermission: () -> Unit
) {
    val state = clientsViewModel.state.observeAsState(State.Initial)
    val clients = clientsViewModel.clients.observeAsState(listOf())
    val context = LocalContext.current
    fun sendToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    if (state.value is State.Error) {
        sendToast((state.value as State.Error).textError)
        clientsViewModel.resetState()
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Button(
                onClick = {
                    requestContactsPermission()
                    getContact()
                },
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.add_client))
            }
        },
        content = { innerPadding ->
            Box(
                Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column {
                    if (clients.value.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(R.string.here_all_clients),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn {
                            items(
                                items = clients.value,
                                key = { it.id.toString() },
                            ) {
                                GetOneClient(
                                    it,
                                    navigationState,
                                    clientsViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun GetOneClient(
    client: Client?,
    navigationState: NavigationState,
    clientsViewModel: ClientsViewModel,
) {
    client?.let {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 15.dp, end = 15.dp)
                .clickable {
                    client.id?.let {
                        clientsViewModel.loadClient(client.id)
                    }
                    navigationState.navigateTo(Destinations.OneClientAllOrders.route)
                },
            elevation = CardDefaults.elevatedCardElevation(6.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(Purple40)
                    .padding(15.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${client.name}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Row {
                    client.phone?.let {
                        Text(
                            text = it.joinToString(", "),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}