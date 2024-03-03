package com.alekseykostyunin.enot.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.navigation.NavigationItem
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel

@Composable
fun AllOrdersScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel
) {
    val orders = ordersViewModel.orders.observeAsState(listOf())
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Button(
                onClick = {
                    navigationState.navigateTo(NavigationItem.AddOrder.route)
//                    navigationState.navHostController.navigate(NavigationItem.AddOrder.route)
                },
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
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
                    .background(Color.White)
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
                    for (i in orders.value) {
                        GetOneOrderListOrders(
                            i,
                            ordersViewModel,
                            navigationState
                        )
                        Log.d("TEST_orders.value", i.toString())
                    }
                }
            }
        }
    )
}

@Composable
fun GetOneOrderListOrders(
    order: Order,
    viewModel: OrdersViewModel,
    navigationState: NavigationState,
)
{
    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                viewModel.getOrderUser(order)
                navigationState.navigateTo(NavigationItem.OneOrder.route)
            },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
    ) {
        Column(
            modifier = Modifier

                .padding(10.dp)
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