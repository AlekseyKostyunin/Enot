package com.alekseykostyunin.enot.presentation.view

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun OrdersScreen(
    navController: NavHostController
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
                    Text(text = "Завершенные")
                }
            }
            OutlinedButton(
                modifier = Modifier.padding(horizontal = 10.dp),
                onClick = {
                    navController.navigate(Destinations.AddOrder.route)
                },
                ) {
                Text(text = "Создать")
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
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        //border = BorderStroke(1.dp, Color.Black),
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
                    onClick = {
                        val auth = Firebase.auth
                        auth.signOut()
                    }
                ) {
                    Text(text = "Закрыть заказ")
                }
            }


            Spacer(modifier = Modifier.imePadding())


        }
    }
}