package com.alekseykostyunin.enot.presentation.screens

import android.widget.Toast
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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.entities.StatusOrder
import com.alekseykostyunin.enot.presentation.general.Circle
import com.alekseykostyunin.enot.presentation.general.ProgressIndicator
import com.alekseykostyunin.enot.presentation.general.ProgressIndicatorLogo
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.presentation.navigation.State
import com.alekseykostyunin.enot.ui.theme.gradient
import kotlin.math.abs

@Composable
fun AllOrdersScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel
) {
    val state = ordersViewModel.state.collectAsState().value
    val orders0 = ordersViewModel.orders.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    fun sendToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    if (state is State.Error) {
        sendToast(state.textError)
        ordersViewModel.resetState()
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(
        stringResource(R.string.active),
        stringResource(R.string.wait),
        stringResource(R.string.all)
    )
    val orders = when (selectedIndex) {
        0 -> {
            orders0.filter { it.status == StatusOrder.OPEN }
        }
        1 -> {
            orders0.filter { it.status == StatusOrder.PAUSED }
        }
        else -> {
            orders0
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
            innerPadding
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                Column {
                    if (state is State.Loading) {
                        ProgressIndicatorLogo()
                    } else if (state is State.Success) {
                        if (orders0.isEmpty()) {
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
                                            Text(
                                                text = label,
                                                fontWeight = FontWeight.Bold,
                                            )
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
                .background(gradient)
                .padding(18.dp)
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
                Circle(color = when (order.status) {
                        StatusOrder.OPEN -> Color.Red
                        StatusOrder.PAUSED -> Color.Yellow
                        StatusOrder.CLOSED -> Color.Green
                    }
                )
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
                    text = stringResource(R.string.pare_parts_ordered),
                    fontWeight = FontWeight.Bold, color = Color.White
                )
                Text(
                    text = when(order.status) {
                        StatusOrder.OPEN -> stringResource(R.string.no2)
                        StatusOrder.PAUSED,StatusOrder.CLOSED-> stringResource(R.string.yes2)
                    },
                    fontWeight = FontWeight.Bold,
                    color = when(order.status) {
                        StatusOrder.OPEN -> Color.Red
                        StatusOrder.PAUSED, StatusOrder.CLOSED-> Color.White
                    }
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
            Row {
                val dateStart = if(order.status == StatusOrder.CLOSED) order.dateClose
                else DateUtil.dateOfUnit
                val difference = abs(dateStart - order.dateAdd)
                val days = difference / (24 * 60 * 60 * 1000)
                Text(
                    text = if(order.status == StatusOrder.CLOSED) stringResource(R.string.completed_for)
                    else stringResource(R.string.in_work),
                    fontWeight = FontWeight.Bold, color = Color.White
                )
                Text(
                    text = days.toString() + stringResource(R.string.day),
                    color = Color.White
                )
            }
        }

    }
}