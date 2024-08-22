package com.alekseykostyunin.enot.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    ordersViewModel: OrdersViewModel
) {
    val orders = ordersViewModel.orders.observeAsState(listOf())
    val countOrders = orders.value.size.toString()

    val prizeZ = orders.value.sumOf {
        it.priceZip
    }
    val profit = orders.value.sumOf {
        it.priceWork
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Row {
                var selectedIndex by remember { mutableStateOf(0) }
                val options = listOf("Заказы", "Клиенты")
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxSize()
                ) {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex
                        ) {
                            Text(text = label)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                var selectedIndex by remember { mutableStateOf(0) }
                val options = listOf("Сегодня", "Неделя", "Месяц", "Полгода", "Год")
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxSize()
                ) {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Text(text = "Общее количество заказов: " + countOrders)
            Text(text = "Общие расходы на запчасти: " + prizeZ + " руб.")
            Text(text = "Общая прибыль со всех заказов: " + profit + " руб.")

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(250.dp)
                    .fillMaxSize()
            ) {
                Grafik()
            }

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(250.dp)
                    .fillMaxSize()
            ) {
                Grafik()
            }


        }

    }
}

@Composable
fun Grafik(){
    LineChart(
        lineChartData = LineChartData(
            points = listOf(
                LineChartData.Point(randomYValue(), "Line 1"),
                LineChartData.Point(randomYValue(), "Line 2"),
                LineChartData.Point(randomYValue(), "Line 3"),
                LineChartData.Point(randomYValue(), "Line 4"),
                LineChartData.Point(randomYValue(), "Line 5"),
                LineChartData.Point(randomYValue(), "Line 6"),
                LineChartData.Point(randomYValue(), "Line 7"),
            )
        ),
        // Optional properties.
        modifier = Modifier.fillMaxSize(),
        animation = simpleChartAnimation(),
        pointDrawer = FilledCircularPointDrawer(),
        lineDrawer = SolidLineDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(),
        horizontalOffset = 5f
    )
}

private fun randomYValue(): Float = Random.Default.nextInt(500, 1000).toFloat()


































