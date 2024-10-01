package com.alekseykostyunin.enot.presentation.screens

import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.R
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
                    text = stringResource(R.string.add_order),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            val expandedClient = remember { mutableStateOf(false) }
            val clients = clientsViewModel.clients
            val selectedOptionTextClient = remember { mutableStateOf("") }
            if (clients.value?.isEmpty() == true) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigationState.navigateTo(Destinations.AllClients.route)
                        }
                ) {
                    Text(
                        text = stringResource(R.string.mess_add_first_client),
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
                        label = { Text(stringResource(R.string.client)) },
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

            var desc by remember { mutableStateOf("") }
            var isErrorDesc by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorDesc,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = desc,
                label = { Text(stringResource(R.string.desc_order)) },
                onValueChange = { newText -> desc = newText },
            )

            val options =
                listOf(
                    stringResource(R.string.mobile_phone),
                    stringResource(R.string.computer),
                    stringResource(R.string.nootbook),
                    stringResource(R.string.television),
                    stringResource(R.string.tablet),
                    stringResource(R.string.other)
                )
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
                    label = { Text(stringResource(R.string.type_order)) },
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

            var model by remember { mutableStateOf("") }
            var isErrorModel by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorModel,
                    modifier = Modifier.fillMaxWidth(),
                    value = model,
                    label = { Text(stringResource(R.string.model)) },
                    onValueChange = { newText -> model = newText },
                )
            }

            var priceZ by remember { mutableStateOf("") }
            var isErrorPriceZ by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorPriceZ,
                    modifier = Modifier.fillMaxWidth(),
                    value = priceZ,
                    label = { Text(stringResource(R.string.price_zip)) },
                    onValueChange = { newText -> priceZ = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            var price by remember { mutableStateOf("") }
            var isErrorPrice by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorPrice,
                    modifier = Modifier.fillMaxWidth(),
                    value = price,
                    label = { Text(stringResource(R.string.price_order2)) },
                    onValueChange = { newText -> price = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            var comment by remember { mutableStateOf("") }
            var isErrorComment by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorComment,
                    modifier = Modifier.fillMaxWidth(),
                    value = comment,
                    label = { Text(stringResource(R.string.comment)) },
                    onValueChange = { newText -> comment = newText },
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = {
                    if (selectedOptionTextClient.value.isEmpty()) {
                        sendToast(context.getString(R.string.error_client_not_empty))
                    } else {
                        if (desc.isEmpty()) {
                            isErrorDesc = true
                            sendToast(context.getString(R.string.error_dest_not_empty))
                        } else {
                            if (selectedOptionTextTypeOrder.isEmpty()) {
                                sendToast(context.getString(R.string.error_type_order_not_empty))
                            } else {
                                if (model.isEmpty()) {
                                    isErrorModel = true
                                    isErrorDesc = false
                                    sendToast(context.getString(R.string.error_model_not_empty))
                                } else {
                                    if (priceZ.isEmpty()) {
                                        isErrorPriceZ = true
                                        isErrorModel = false
                                        sendToast(context.getString(R.string.error_priceZ_not_empty))
                                    } else {
                                        if (!Validate.isNumericToX(priceZ)) {
                                            sendToast(context.getString(R.string.error_incorrect_number_try_again))
                                        } else {
                                            if (price.isEmpty()) {
                                                isErrorPrice = true
                                                isErrorPriceZ = false
                                                sendToast(context.getString(R.string.error_price_not_empty))
                                            } else {
                                                if (!Validate.isNumericToX(price)) {
                                                    sendToast(context.getString(R.string.error_incorrect_number_try_again))
                                                } else {
                                                    if (comment.isEmpty()) {
                                                        isErrorComment = true
                                                        sendToast(context.getString(R.string.error_comment_not_empty))
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
                Text(stringResource(R.string.add))
            }
        }
    }
}


