package com.alekseykostyunin.enot.presentation.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.data.utils.Validate
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.navigation.NavigationItem
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel
) {
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
                        navigationState.navigateTo(NavigationItem.AllOrders.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
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
            var client by remember { mutableStateOf("") }
            var isErrorClient by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorClient,
                modifier = Modifier.fillMaxWidth(),
                value = client,
                label = { Text("Клиент") },
                onValueChange = { newText -> client = newText },
            )

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
            val options = listOf("сотовый телефон", "компьютер", "ноутбук", "телевизор","планшет","иное")
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf("") }

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
                        .fillMaxWidth()
                    ,
                    readOnly = true,
                    value = selectedOptionText,
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
                                selectedOptionText = selectionOption
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

            /* Кнопка "Добавить" */
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = {
                    if (client.isEmpty()) {
                        isErrorClient = true
                        sendToast("Поле клиент не может быть пустым!")
                    } else {
                        if (desc.isEmpty()) {
                            isErrorDesc = true
                            isErrorClient = false
                            sendToast("Поле описание не может быть пустым!")
                        } else {
                            if(selectedOptionText.isEmpty()){
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
                                        if (!Validate.isNumericToX(priceZ)){
                                            sendToast("Некорректное число! Повторите попытку.")
                                        } else{
                                            if (price.isEmpty()) {
                                                isErrorPrice = true
                                                isErrorPriceZ = false
                                                sendToast("Поле стоимость заказа не может быть пустым!")
                                            } else {
                                                if(!Validate.isNumericToX(price)){
                                                    sendToast("Некорректное число! Повторите попытку.")
                                                } else {
                                                    val auth: FirebaseAuth = Firebase.auth
                                                    val database = Firebase.database.reference
                                                    val user = auth.currentUser
                                                    if (user != null) {
                                                        val userId = user.uid
                                                        val idOrder = database.child("users").child(userId)
                                                            .child("orders").push().key.toString()
                                                        val order = Order(
                                                            id = idOrder,
                                                            client = client,
                                                            dateAdd = DateUtil.dateOfUnit,
                                                            dateClose = "no",
                                                            description = desc,
                                                            type = selectedOptionText,
                                                            model = model,
                                                            priceZip = priceZ.toInt(),
                                                            priceWork = price.toInt(),
                                                            isWork = true
                                                        )
                                                        database.child("users").child(userId).child("orders")
                                                            .child(idOrder).setValue(order)
                                                    }

                                                    ordersViewModel.updateOrders()
                                                    ordersViewModel.showBottomBar()
                                                    navigationState.navigateTo(NavigationItem.Orders.route)

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


