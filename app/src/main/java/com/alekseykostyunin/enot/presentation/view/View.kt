package com.alekseykostyunin.enot.presentation.view

import android.widget.NumberPicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

/* Заказы  ***********************************************/

@Composable
fun Orders(
    //navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .padding(30.dp)
            .padding(horizontal = 30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(
                text = "Заказы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { }
                ) {
                    Text(text = "В работе")
                }
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { },

                    ) {
                    Text(text = "Завершенные")
                }
            }

            for (i in 0 until 15) {
                GetOneOrder(number = i)
            }
            Spacer(modifier = Modifier.imePadding())
        }

    }

}


@Composable
fun GetOneOrder(number: Int) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .size(width = 340.dp, height = 100.dp)
            .padding(vertical = 10.dp),
    ) {
        Text(
            text = "Заказ № $number от 21.12.2023",
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}


@Composable
fun Order() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {

        Column {
            Text(
                text = "Заказ № 001 от 21.11.2023",
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
                    text = "Клиент:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Фамилия Имя Отчество",

                    )
                Text(
                    text = "Позвонить",

                    )
            }
            Row {
                Text(
                    text = "Описание: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "замена дисплея",
                )
            }

            Row {
                Text(
                    text = "Тип заказа: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "сотовый телефон",
                )
            }

            Row {
                Text(
                    text = "Модель: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Infinix 789",
                )
            }

            Row {
                Text(
                    text = "Цена запчастей: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1500 руб.",
                )
            }

            Row {
                Text(
                    text = "Стоимость заказа: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "500 руб.",
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
                    onClick = { }
                ) {
                    Text(text = "Закрыть заказ")
                }
            }


            Spacer(modifier = Modifier.imePadding())


        }
    }
}

@Composable
fun AddOrder(
    //navController: NavController
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

                Column(Modifier.selectableGroup())
                {
                    typeOrder.forEach { text ->
                        Row(verticalAlignment = Alignment.CenterVertically)
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

            Column(modifier = Modifier.padding(vertical = 10.dp)){
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
            Button(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), onClick = { }) {
                Text(text = "Добавить")
            }

            //Spacer(modifier = Modifier.imePadding())


        }

    }
}


/* Клиенты  *********************************************/

@Composable
fun Clients(
    //navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(
                text = "Клиенты",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { }
                ) {
                    Text(text = "Последние")
                }
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { },

                    ) {
                    Text(text = "По алфавиту")
                }
            }
            for (i in 0 until 15) {
                GetOneClient(number = i)
            }
            Spacer(modifier = Modifier.imePadding())
        }

    }

}


@Composable
fun GetOneClient(number: Int) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(0.1.dp, Color.Black),
        modifier = Modifier
            //.size(width = 340.dp, height = 100.dp)
            .fillMaxSize()
            .padding(vertical = 10.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Фамилия Имя Отчество № $number",

            )
    }
}