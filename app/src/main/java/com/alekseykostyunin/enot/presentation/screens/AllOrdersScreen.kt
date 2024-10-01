package com.alekseykostyunin.enot.presentation.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.general.Circle
import com.alekseykostyunin.enot.presentation.general.ProgressIndicator
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.State
import com.alekseykostyunin.enot.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllOrdersScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel
) {
    val state = ordersViewModel.state.observeAsState(State.Initial)
    val orders0 = ordersViewModel.orders.observeAsState(listOf())
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(
        stringResource(R.string.active),
        stringResource(R.string.closed),
        stringResource(R.string.all)
    )
    val orders = when (selectedIndex) {
        0 -> {
            orders0.value.filter { it.isWork }
        }

        1 -> {
            orders0.value.filter { !it.isWork }
        }

        else -> {
            orders0.value
        }
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Button(
                onClick = {
                    ordersViewModel.notShowBottomBar()
                    navigationState.navigateTo(Destinations.AddOrder.route)
                },
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.create_order))
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column {
                    if (state.value == State.Loading) {
                        ProgressIndicator()
                    } else {
                        if (orders0.value.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    stringResource(R.string.here_all_orders),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                                    options.forEachIndexed { index, label ->
                                        SegmentedButton(
                                            shape = SegmentedButtonDefaults.itemShape(
                                                index = index,
                                                count = options.size
                                            ),
                                            onClick = { selectedIndex = index },
                                            selected = index == selectedIndex,

                                        ) {
                                            Text(label)
                                        }
                                    }
                                }
                            }
                            LazyColumn {
                                items(
                                    items = orders,
                                    key = { it.id.toString() }
                                ) {
                                    GetOneOrderListOrders(
                                        it,
                                        ordersViewModel,
                                        navigationState
                                    )
                                }
                            }
                        }
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
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
            .clickable {
                viewModel.getOrderUser(order)
                navigationState.navigateTo(Destinations.OneOrder.route)
            },
        elevation = CardDefaults.elevatedCardElevation(6.dp),
    ) {
        Column(
            modifier = Modifier
                .background(Purple40)
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dt = DateUtil.dateFormatter(order.dateAdd.toString())
                Text(
                    text = stringResource(R.string.order_from, dt),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Circle(color = if (order.isWork) Color.Red else Color.Green)
            }
            Row {
                Text(
                    text = stringResource(R.string.client_),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = order.client?.name.toString(),
                    color = Color.White
                )
            }
            Row {
                Text(
                    text = stringResource(R.string.desc_),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = order.description.toString(),
                    color = Color.White
                )
            }
            Row {
                Text(
                    text = stringResource(R.string.type_orders_),
                    fontWeight = FontWeight.Bold, color = Color.White
                )
                Text(
                    text = order.type.toString(),
                    color = Color.White
                )
            }
            Row {
                Text(
                    text = stringResource(R.string.price_order),
                    fontWeight = FontWeight.Bold, color = Color.White
                )
                Text(
                    text = order.priceWork.toString() + stringResource(R.string._rub),
                    color = Color.White
                )
            }
        }

    }
}