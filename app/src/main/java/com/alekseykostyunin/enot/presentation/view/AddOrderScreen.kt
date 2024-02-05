package com.alekseykostyunin.enot.presentation.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun AddOrderScreen(
    navController: NavController
) {
    val context = LocalContext.current
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
                    if(client.isEmpty()) {
                        isErrorClient = true
                        sendToast("Поле клиент не может быть пустым!", context)
                    }
                    else {
                        if (desc.isEmpty()){
                            isErrorDesc = true
                            isErrorClient = false
                            sendToast("Поле описание не может быть пустым!",context)
                        }
                        else {
                            if (model.isEmpty()){
                                isErrorModel = true
                                isErrorDesc = false
                                sendToast("Поле модель не может быть пустым!",context)
                            }
                            else {
                                if (priceZ.isEmpty()){
                                    isErrorPriceZ = true
                                    isErrorModel = false
                                    sendToast("Поле цена запчастей не может быть пустым!",context)
                                }
                                else {
                                    if (price.isEmpty()){
                                        isErrorPrice = true
                                        isErrorPriceZ = false
                                        sendToast(
                                            "Поле стоимость заказа не может быть пустым!",
                                            context
                                        )
                                    }else{
                                        val auth: FirebaseAuth = Firebase.auth
                                        val database = Firebase.database.reference
                                        val user = auth.currentUser
                                        if(user != null){
                                            val userId = user.uid
                                            val idOrder = database.child("users").child(userId).child("orders").push().key.toString()
                                            val order = Order(
                                                id = idOrder, dateAdd = DateUtil.dateOfUnit, dateClose = "no", description = desc,
                                                type = selectedOption, model = model, priceZip = priceZ.toInt(), priceWork = price.toInt(), isWork = true)
                                            database.child("users").child(userId).child("orders").child(idOrder).setValue(order)
                                        }
                                        navController.navigate(Destinations.Orders.route)
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

private fun sendToast(message: String, context: Context) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG,
    ).show()
}