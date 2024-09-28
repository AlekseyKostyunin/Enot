package com.alekseykostyunin.enot.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.data.utils.Validate
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel,
    clientsViewModel: ClientsViewModel
) {
    clientsViewModel.updateClients()
    val clientOfDb = remember { mutableStateOf(Client()) }

    val context = LocalContext.current
    fun sendToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Row {
                IconButton(onClick = {
                    ordersViewModel.showBottomBar()
                    navigationState.navigateTo(Destinations.AllOrders.route)
                }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    text = "Добавление заказа:",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            /* Клиент */
            var expandedClient = remember { mutableStateOf(false) }
            val clients = clientsViewModel.clients
            var selectedOptionTextClient = remember { mutableStateOf("") }
            if (clients.value?.isEmpty() == true) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigationState.navigateTo(Destinations.AllClients.route)
                        }
                ) {
                    Text(
                        text = "Добавьте первого клиента",
                        modifier = Modifier.padding(18.dp),
                        fontSize = 16.sp,
                    )
                }

            } else {
                ExposedDropdownMenuBox(
                    modifier = Modifier.padding(top = 10.dp),
                    expanded = expandedClient.value,
                    onExpandedChange = {
                        expandedClient.value = !expandedClient.value
                    }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = selectedOptionTextClient.value,
                        onValueChange = { },
                        label = { Text("Клиент") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedClient.value
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedClient.value,
                        onDismissRequest = {
                            expandedClient.value = false
                        }
                    ) {
                        clients.value?.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = selectionOption.name.toString())
                                },
                                onClick = {
                                    selectedOptionTextClient.value = selectionOption.name.toString()
                                    expandedClient.value = false
                                    clientOfDb.value = selectionOption
                                }
                            )
                        }
                    }
                }


            }

//            var isErrorClient by rememberSaveable { mutableStateOf(false) }
//            OutlinedTextField(
//                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
//                isError = isErrorClient,
//                modifier = Modifier.fillMaxWidth(),
//                value = client,
//                label = { Text("Клиент") },
//                onValueChange = { newText -> client = newText },
//                supportingText = null
//            )


//            fun validate(text: CharSequence) {
//                isError = text.length > charLimit
//            }
            LaunchedEffect(Unit) {
                // Run validation whenever text value changes
                //snapshotFlow { state.text }.collect { validate(it) }
            }

            /* Описание */
            var desc by remember { mutableStateOf("") }
            var isErrorDesc by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorDesc,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = desc,
                label = { Text("Описание заказа") },
                onValueChange = { newText -> desc = newText },
            )

            /* Тип заказа */
            val options =
                listOf("сотовый телефон", "компьютер", "ноутбук", "телевизор", "планшет", "иное")
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionTextTypeOrder by remember { mutableStateOf("") }

            ExposedDropdownMenuBox(
                modifier = Modifier.padding(top = 10.dp),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedOptionTextTypeOrder,
                    onValueChange = { },
                    label = { Text("Тип заказа") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = selectionOption)
                            },
                            onClick = {
                                selectedOptionTextTypeOrder = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }


            /* Модель */
            var model by remember { mutableStateOf("") }
            var isErrorModel by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorModel,
                    modifier = Modifier.fillMaxWidth(),
                    value = model,
                    label = { Text("Модель") },
                    onValueChange = { newText -> model = newText },
                )
            }

            /* Цена запчастей */
            var priceZ by remember { mutableStateOf("") }
            var isErrorPriceZ by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorPriceZ,
                    modifier = Modifier.fillMaxWidth(),
                    value = priceZ,
                    label = { Text("Цена запчастей") },
                    onValueChange = { newText -> priceZ = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            /* Стоимость заказа */
            var price by remember { mutableStateOf("") }
            var isErrorPrice by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorPrice,
                    modifier = Modifier.fillMaxWidth(),
                    value = price,
                    label = { Text("Стоимость заказа") },
                    onValueChange = { newText -> price = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            /* Комментарий */
            var comment by remember { mutableStateOf("") }
            var isErrorComent by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorComent,
                    modifier = Modifier.fillMaxWidth(),
                    value = comment,
                    label = { Text("Коментарий") },
                    onValueChange = { newText -> comment = newText },
                )
            }

            /* Кнопка "Добавить" */
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = {
                    if (selectedOptionTextClient.value.isEmpty()) {
                        sendToast("Поле клиент не может быть пустым!")
                    } else {
                        if (desc.isEmpty()) {
                            isErrorDesc = true
                            sendToast("Поле описание не может быть пустым!")
                        } else {
                            if (selectedOptionTextTypeOrder.isEmpty()) {
                                sendToast("Поле типа заказа не может быть пустым!")
                            } else {
                                if (model.isEmpty()) {
                                    isErrorModel = true
                                    isErrorDesc = false
                                    sendToast("Поле модель не может быть пустым!")
                                } else {
                                    if (priceZ.isEmpty()) {
                                        isErrorPriceZ = true
                                        isErrorModel = false
                                        sendToast("Поле цена запчастей не может быть пустым!")
                                    } else {
                                        if (!Validate.isNumericToX(priceZ)) {
                                            sendToast("Некорректное число! Повторите попытку.")
                                        } else {
                                            if (price.isEmpty()) {
                                                isErrorPrice = true
                                                isErrorPriceZ = false
                                                sendToast("Поле стоимость заказа не может быть пустым!")
                                            } else {
                                                if (!Validate.isNumericToX(price)) {
                                                    sendToast("Некорректное число! Повторите попытку.")
                                                } else {
                                                    if (comment.isEmpty()) {
                                                        isErrorComent = true
                                                        sendToast("Поле комментарий не может быть пустым!")
                                                    } else {
                                                        val auth: FirebaseAuth = Firebase.auth
                                                        val database = Firebase.database.reference
                                                        val user = auth.currentUser
                                                        if (user != null) {
                                                            val userId = user.uid
                                                            val idOrder = database.child("users")
                                                                .child(userId)
                                                                .child("orders")
                                                                .push().key.toString()
                                                            val dateAdd = DateUtil.dateOfUnit
                                                            val historyStep1 = HistoryStep(
                                                                0,
                                                                dateAdd,
                                                                2,
                                                                "Заказ создан"
                                                            )
                                                            val history = listOf(historyStep1)
                                                            val clientNew = Client(
                                                                clientOfDb.value.id,
                                                                clientOfDb.value.name,
                                                                clientOfDb.value.phone
                                                            )
                                                            val order = Order(
                                                                id = idOrder,
                                                                client = clientNew,
                                                                dateAdd = dateAdd,
                                                                dateClose = 0,
                                                                description = desc,
                                                                type = selectedOptionTextTypeOrder,
                                                                model = model,
                                                                priceZip = priceZ.toInt(),
                                                                priceWork = price.toInt(),
                                                                isWork = true,
                                                                history = history,
                                                                comment = comment,
                                                            )
                                                            database.child("users").child(userId)
                                                                .child("orders")
                                                                .child(idOrder).setValue(order)
                                                        }

                                                        ordersViewModel.updateOrders()
                                                        ordersViewModel.showBottomBar()
                                                        navigationState.navigateTo(Destinations.Orders.route)
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            ) {
                Text(text = "Добавить")
            }
        }
    }
}


