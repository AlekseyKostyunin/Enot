package com.alekseykostyunin.enot.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel

@Composable
fun OneOrderScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel
){
    val orderLD = ordersViewModel.order.observeAsState()
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