package com.alekseykostyunin.enot.presentation.view

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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AddOrderScreen(
    navController: NavController
) {
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
                    fontSize = 18.sp,
                )
                Text(
                    text = "Выбрать",
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontSize = 18.sp,
                )
            }

            /* Описание */

            Text(
                text = "Описание:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                //modifier = Modifier.padding(vertical = 10.dp),

            )
            val desc = remember { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = desc.value,
                placeholder = { Text("Введите описание") },
                onValueChange = { newText -> desc.value = newText },
            )

            /* Тип заказа */
            Row()
            {
                Text(
                    text = "Тип заказа:",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                val typeOrder =
                    listOf("сотовый телефон", "компьютер", "ноутбук", "телевизор", "иное")
                val (selectedOption, onOptionSelected) = remember { mutableStateOf(typeOrder[0]) }

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

            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Модель:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                val type = remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = type.value,
                    placeholder = { Text("Введите модель") },
                    onValueChange = { newText -> type.value = newText },
                )
            }

            /* Цена запчастей */
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Цена запчастей:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                val priceZ = remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = priceZ.value,
                    placeholder = { Text("Введите цену") },
                    onValueChange = { newText -> priceZ.value = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            /* Стоимость заказа */
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Стоимость заказа:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                val price = remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = price.value,
                    placeholder = { Text("Введите стоимость") },
                    onValueChange = { newText -> price.value = newText },
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
                    navController.navigate(Destinations.Orders.route)
                }
            ) {
                Text(text = "Добавить")
            }
        }
    }
}