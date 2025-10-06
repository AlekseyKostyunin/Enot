package com.alekseykostyunin.enot.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.data.utils.Validate
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.entities.StatusOrder
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.navigation.State
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.ui.theme.gradient
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel,
    clientsViewModel: ClientsViewModel,
    getContact: () -> Unit,
    requestContactsPermission: () -> Unit
) {
    //clientsViewModel.updateClients()
    val state = clientsViewModel.state.collectAsStateWithLifecycle().value
    val clientOfDb = remember { mutableStateOf(Client()) }
    val context = LocalContext.current
    fun sendToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    if (state is State.Error) {
        sendToast(state.textError)
        clientsViewModel.resetState()
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
                }) {
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
            val clients = clientsViewModel.clients.collectAsStateWithLifecycle().value
            val selectedOptionTextClient = remember { mutableStateOf("") }

            if (clients.isEmpty()) {
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
                var openBottomSheet by rememberSaveable { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable { openBottomSheet = true },
                    expanded = expandedClient.value,
                    onExpandedChange = {
                        //expandedClient.value = !expandedClient.value
                        openBottomSheet = true
                    },

                    ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable, true)
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
                    )
                }

                // Начало - Нижнее выплывающее окно для списка клиентов
                var skipPartiallyExpanded by rememberSaveable {
                    mutableStateOf(true) }
                val bottomSheetState =
                    rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

                if (openBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { openBottomSheet = false },
                        sheetState = bottomSheetState
                    ) {
                        var text by remember { mutableStateOf("") }
                        val filteredClients = clients.asSequence().filter {
                            it.name?.lowercase(Locale.getDefault())?.contains(
                                text.lowercase(Locale.getDefault())
                            ) ?: false
                        }.toList()

                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            label = { Text("Поиск") },
                        )
                        // Кнопка для добавления нового клиента
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    requestContactsPermission()
                                    getContact()
                                }
                        ) {
                            Text(
                                text = "Добавить нового клиента",
                                modifier = Modifier.padding(18.dp),
                                fontSize = 16.sp,
                            )
                        }

                        LazyColumn {
                            items(
                                items = filteredClients,
                                key = { client ->
                                    client.id.toString()
                                },
                            ) { client ->
                                GetOneClientForSearch(
                                    client,
                                    onClientSelected = {
                                        selectedOptionTextClient.value = it.name ?: ""
                                        clientOfDb.value = it
                                        openBottomSheet = false // Закрытие бутсшита после выбора
                                    }
                                )
                            }

                        }
                    }
                }
                // Конец - Нижнее выплывающее окно для списка клиентов
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
                    stringResource(R.string.print),
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
                        .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedOptionTextTypeOrder,
                    onValueChange = { },
                    label = { Text(stringResource(R.string.type_order)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    }
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
                                                            status = StatusOrder.OPEN,
                                                            client = clientNew,
                                                            dateAdd = dateAdd,
                                                            dateClose = 0,
                                                            description = desc,
                                                            type = selectedOptionTextTypeOrder,
                                                            model = model,
                                                            priceZip = priceZ.toInt(),
                                                            priceWork = price.toInt(),
                                                            history = history,
                                                            comment = comment,
                                                        )
                                                        ordersViewModel.addOrder(order)
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

@Composable
fun GetOneClientForSearch(
    client: Client,
    onClientSelected: (Client) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp, start = 15.dp, end = 15.dp)
            .clickable {
                onClientSelected(client)
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
