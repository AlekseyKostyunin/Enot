package com.alekseykostyunin.enot.presentation.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.sharp.Call
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.presentation.general.ProgressIndicator
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.State

@Composable
fun OneClientAllOrdersScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel,
    clientsViewModel: ClientsViewModel,
    requestCallPhonePermission: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val state = clientsViewModel.state.observeAsState(State.Initial)
    val client by clientsViewModel.client.observeAsState(Client())
    client.id?.let { ordersViewModel.getOneClientAllOrdersOnId(it) }
    val allOrdersOneClient by ordersViewModel.oneClientAllOrders.observeAsState(listOf())

    val openDialogNotNumberPhone = remember { mutableStateOf(false) }
    val openDialogSelectNumberPhone = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                //horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navigationState.navigateTo(Destinations.AllClients.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                client.name?.let {
                    Text(
                        text = it,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = {
                        navigationState.navigateTo(Destinations.EditClient.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        requestCallPhonePermission()
                        if (client.phone != null) {
                            if (client.phone!![0] == "нет номера телефона") {
                                openDialogNotNumberPhone.value = true
                            } else {
                                openDialogSelectNumberPhone.value = true
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Call,
                        contentDescription = null
                    )
                }
            }

            if (state.value == State.Loading) {
                ProgressIndicator()
            } else {
                if (allOrdersOneClient.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Отсутствуют заказы по данному клиенту",
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn {
                        items(
                            items = allOrdersOneClient,
                            key = { it.id.toString() }
                        ) {
                            GetOneOrderListOrders(
                                it,
                                ordersViewModel,
                                navigationState
                            )
                        }
                    }
                }
            }
        }
    }

    if (openDialogNotNumberPhone.value) {
        Dialog(
            onDismissRequest = {
                openDialogNotNumberPhone.value = false
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Отсутствует номер телефона",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = {
                                openDialogNotNumberPhone.value = false
                            },
                            modifier = Modifier.padding(end = 8.dp),
                        ) {
                            Text("Закрыть")
                        }
                    }
                }
            }
        }
    }

    if (openDialogSelectNumberPhone.value) {
        Dialog(
            onDismissRequest = {
                openDialogSelectNumberPhone.value = false
            }
        ) {
            OutlinedCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Позвонить по номеру",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    )
                    for (oneNumberPhone in client.phone!!) {
                        ElevatedCard(
                            onClick = {
                                val intent = Intent(Intent.ACTION_CALL)
                                intent.data = Uri.parse("tel:$oneNumberPhone")
                                activity.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    oneNumberPhone,
                                    Modifier.align(Alignment.Center),
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}