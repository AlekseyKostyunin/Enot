package com.alekseykostyunin.enot.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.startActivityForResult
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.general.ProgressIndicator
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.MainViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(
    navigationState: NavigationState,
    mainViewModel: MainViewModel,
    clientsViewModel: ClientsViewModel,
    getContact: () -> Unit,
    requestContactsPermission: () -> Unit,
    requestCallPhonePermission: () -> Unit,
) {
    val state = clientsViewModel.state.observeAsState(State.Initial)
    var contactName by mutableStateOf("")
    var contactNumber by mutableStateOf("")
    val clients by clientsViewModel.clients.observeAsState(emptyList())
    val client by clientsViewModel.client.observeAsState(Client())

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
                Text(text = "Добавить")
            }
        },
        content = { innerPadding ->
            Box(Modifier.background(Color.White).fillMaxSize().padding(innerPadding)
            ) {
                Column {
                    if (state.value == State.Loading) {
                        ProgressIndicator()
                    } else {
                        Column {
                            if (clients.isEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) { Text("Здесь будут отображаться все клиенты",
                                    textAlign = TextAlign.Center ) }
                            } else {
                                Row(
                                    modifier = Modifier.padding(top = 15.dp, start = 15.dp, end = 15.dp)
                                ) {
                                    var selectedIndex by remember { mutableIntStateOf(0) }
                                    val options = listOf("Последние", "По алфавиту")
                                    SingleChoiceSegmentedButtonRow(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        options.forEachIndexed { index, label ->
                                            SegmentedButton(
                                                shape = SegmentedButtonDefaults.itemShape(
                                                    index = index,
                                                    count = options.size
                                                ),
                                                onClick = { selectedIndex = index },
                                                selected = index == selectedIndex
                                            ) {
                                                Text(label)
                                            }
                                        }
                                    }
                                }
//                                for (i in clients) {
//                                    GetOneClient(
//                                        i,
//                                        navigationState,
//                                        clientsViewModel
//                                    )
//                                }
                                LazyColumn {
                                    items(
                                        items = clients,
                                        key = { it.id.toString()},
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
    val gradient = Brush.horizontalGradient(
        0.0f to Color(0xFF04293A),
        1.0f to Color(0xFF781D42),
        startX = 1000.0f,
        endX = 0.0f
    )
    client?.let {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 15.dp, end = 15.dp)
                .clickable {
                    client.id?.let {
                        id -> clientsViewModel.loadClient(id)
                    }
                    //navigationState.navigateTo(Destinations.OneOrder.route)
                },
            elevation = CardDefaults.elevatedCardElevation(6.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(gradient)
                    .padding(15.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Клиент: ${client.name}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Row {
                    Text(
                        text = "Номер телефона: ",
                        fontWeight = FontWeight.Bold, color = Color.White
                    )
                    client.phone?.let {
                        Text(
                            text = it,
                            color = Color.White
                        )
                    }
                }
            }

        }
    }
}