package com.alekseykostyunin.enot.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.presentation.navigation.NavigationItem
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel

@Composable
fun OneOrderScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel
){
    val orderLD = ordersViewModel.order.observeAsState()
    val order = orderLD.value
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ElevatedButton(
                onClick = {
                    //navigationState.navigateTo(NavigationItem.AddOrder.route)
                },
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Добавить шаг")
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
                    .padding(innerPadding)
                    ,
            ) {
                Column {
                    val dt = DateUtil.dateFormatter(order?.dateAdd.toString())
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Заказ от $dt",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(vertical = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = {
                                navigationState.navigateTo(NavigationItem.EditOrder.route)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = null
                            )
                        }
                    }

                    Row {
                        Text(
                            text = "Статус: ",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "активный",
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                    ) {
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
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                    ) {
                        Text(
                            text = "Описание: ",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = order?.description.toString(),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                    ) {
                        Text(
                            text = "Тип заказа: ",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = order?.type.toString(),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                    ) {
                        Text(
                            text = "Модель: ",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = order?.model.toString(),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                    ) {
                        Text(
                            text = "Цена запчастей: ",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = order?.priceZip.toString(),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                    ) {
                        Text(
                            text = "Стоимость заказа: ",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = order?.priceWork.toString(),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Фото:",
                            fontWeight = FontWeight.Bold,
                        )
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )


                        }
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = null
                            )
                        }

                    }

                    Text(
                        text = "Исполнение заказа:",
                        modifier = Modifier.padding(vertical = 10.dp),
                        fontWeight = FontWeight.Bold
                    )



                }
            }
        }
    )
}