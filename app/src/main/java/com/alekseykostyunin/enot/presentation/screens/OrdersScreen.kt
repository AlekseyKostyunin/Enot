package com.alekseykostyunin.enot.presentation.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.navigation.OrdersScreenState
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun OrdersScreenMenu() {
    val ordersViewModel: OrdersViewModel = viewModel()
    val screenState = ordersViewModel.ordersState.observeAsState(OrdersScreenState.OrdersState)
    when (screenState.value) {
        OrdersScreenState.OrdersState -> {ScreenOrders(ordersViewModel)}
        OrdersScreenState.AddOrderState -> {ScreenAddOrder(ordersViewModel)}
        OrdersScreenState.OneOrderState -> {ScreenOneOrder(ordersViewModel)}
    }
}
@Composable
fun ScreenOrders(viewModel: OrdersViewModel) {
    val orders = viewModel.orders.observeAsState(listOf())
    Scaffold(
        modifier = Modifier
            .padding(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ElevatedButton(
                onClick = {
                    viewModel.setAddOrderState()
                },
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Создать")
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    Row(
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {

                            }
                        ) {
                            Text(text = "В работе")
                        }
                        OutlinedButton(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            onClick = {

                            },

                            ) {
                            Text(text = "Закрытые")
                        }
                    }
                    //viewModel.getAllOrdersUser()
                    for (i in orders.value) {
                        GetOneOrderListOrders(i, viewModel)
                        Log.d("TEST_orders.value", i.toString())
                    }
                }
            }
        }
    )
}
@Composable
fun ScreenAddOrder(viewModel: OrdersViewModel) {
    val context = LocalContext.current
    fun sendToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(
                text = "Добавление заказа:",
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Клиент:",
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Выбрать",
                    modifier = Modifier.padding(horizontal = 10.dp),
                )
            }
            var client by remember { mutableStateOf("") }
            var isErrorClient by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorClient,
                modifier = Modifier.fillMaxWidth(),
                value = client,
                placeholder = { Text("Введите клиента") },
                onValueChange = { newText -> client = newText },
            )

            /* Описание */
            Text(
                text = "Описание:",
                fontWeight = FontWeight.Bold,

                )
            var desc by remember { mutableStateOf("") }
            var isErrorDesc by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorDesc,
                modifier = Modifier.fillMaxWidth(),
                value = desc,
                placeholder = { Text("Введите описание") },
                onValueChange = { newText -> desc = newText },
            )

            /* Тип заказа */
            val typeOrder =
                listOf("сотовый телефон", "компьютер", "ноутбук", "телевизор", "иное")
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(typeOrder[0]) }
            Row()
            {
                Text(
                    text = "Тип заказа:",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.Bold,
                )

                Column(
                    Modifier.selectableGroup()
                )
                {
                    typeOrder.forEach { text ->
                        Row(
                            modifier = Modifier.clickable {
                                onOptionSelected(text)
                            },
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                            )
                            Text(text)
                        }
                    }
                }

            }
            /* Модель */
            var model by remember { mutableStateOf("") }
            var isErrorModel by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Модель:",
                    fontWeight = FontWeight.Bold,
                )
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorModel,
                    modifier = Modifier.fillMaxWidth(),
                    value = model,
                    placeholder = { Text("Введите модель") },
                    onValueChange = { newText -> model = newText },
                )
            }

            /* Цена запчастей */
            var priceZ by remember { mutableStateOf("") }
            var isErrorPriceZ by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Цена запчастей:",
                    fontWeight = FontWeight.Bold,
                )
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorPriceZ,
                    modifier = Modifier.fillMaxWidth(),
                    value = priceZ,
                    placeholder = { Text("Введите цену") },
                    onValueChange = { newText -> priceZ = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            /* Стоимость заказа */
            var price by remember { mutableStateOf("") }
            var isErrorPrice by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Стоимость заказа:",
                    fontWeight = FontWeight.Bold,
                )

                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorPrice,
                    modifier = Modifier.fillMaxWidth(),
                    value = price,
                    placeholder = { Text("Введите стоимость") },
                    onValueChange = { newText -> price = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            /* Кнопка "Добавить" */
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 10.dp
                    ),
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
                                    if (price.isEmpty()) {
                                        isErrorPrice = true
                                        isErrorPriceZ = false
                                        sendToast("Поле стоимость заказа не может быть пустым!")
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
                                                type = selectedOption,
                                                model = model,
                                                priceZip = priceZ.toInt(),
                                                priceWork = price.toInt(),
                                                isWork = true
                                            )
                                            database.child("users").child(userId).child("orders")
                                                .child(idOrder).setValue(order)
                                        }
//                                        navController.navigate(NavigationItem.Orders.route)
                                        viewModel.setOrdersState()
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
@Composable
fun ScreenOneOrder(viewModel: OrdersViewModel){
    val orderLD = viewModel.order.observeAsState()
    val order = orderLD.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Column {
            val dt = DateUtil.dateFormatter(order?.dateAdd.toString())
            Text(
                text = "Заказ от $dt",
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.Bold
            )
            Row {
                Text(
                    text = "Статус: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "активный",
                )
            }

            Row {
                Text(
                    text = "Клиент: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order?.client.toString(),

                    )
                Text(
                    text = " Позвонить",

                    )
            }
            Row {
                Text(
                    text = "Описание: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order?.description.toString(),
                )
            }

            Row {
                Text(
                    text = "Тип заказа: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order?.type.toString(),
                )
            }

            Row {
                Text(
                    text = "Модель: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order?.model.toString(),
                )
            }

            Row {
                Text(
                    text = "Цена запчастей: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order?.priceZip.toString(),
                )
            }

            Row {
                Text(
                    text = "Стоимость заказа: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order?.priceWork.toString(),
                )
            }

            Text(
                text = "История: ",
                modifier = Modifier.padding(vertical = 10.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Описание истории ",
            )
            /* Кнопка "Редактировать" */
            OutlinedButton(onClick = { }) {
                Text(text = "Редактировать историю")
            }
            Row(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Фото",
                    fontWeight = FontWeight.Bold,
                )
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { }
                ) {
                    Text(text = "Просмотреть")
                }
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { }
                ) {
                    Text(text = "Добавить")
                }

            }

            /* Кнопка "Редактировать" */
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { }
            ) {
                Text(text = "Редактировать")
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {

                    }
                ) {
                    Text(text = "Закрыть заказ")
                }
            }


        }
    }
}
@Composable
fun GetOneOrderListOrders(order: Order, viewModel: OrdersViewModel) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(vertical = 10.dp)
            .padding(10.dp)
            .clickable {
                //val idOrder = order.id.toString()
                //OrderOneScreen(idOrder)
                //navController.navigate(Destinations.OneOrder.route)
                viewModel.getOrderUser(order)
                viewModel.setOneOrderState()
            },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dt = DateUtil.dateFormatter(order.dateAdd.toString())
                Text(
                    text = "Заказ: от $dt",
                )
                Text(
                    text = "В работе",
                    color = Color.Red
                )
            }


            Row {
                Text(
                    text = "Клиент: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.client.toString()
                )

            }
            Row {
                Text(
                    text = "Описание: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.description.toString(),
                )
            }

            Row {
                Text(
                    text = "Тип заказа: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.type.toString(),
                )
            }
        }

    }
}