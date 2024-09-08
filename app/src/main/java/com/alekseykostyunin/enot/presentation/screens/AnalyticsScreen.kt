package com.alekseykostyunin.enot.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import android.text.Layout
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alekseykostyunin.enot.data.utils.DateUtil.Companion.dateFormatter
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberLayeredComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShadow
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.shape.markerCornered
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.HorizontalDimensions
import com.patrykandpatrick.vico.core.cartesian.Insets
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.Legend
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.copyColor
import com.patrykandpatrick.vico.core.common.shape.Corner
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import me.bytebeats.views.charts.line.LineChartData
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    ordersViewModel: OrdersViewModel,
    clientsViewModel: ClientsViewModel
) {
    val currentDate = System.currentTimeMillis()
    val weerAgo = (currentDate / 1000 - (60 * 60 * 24 * 7)) * 1000
    val monthAgo = (currentDate / 1000 - (60 * 60 * 24 * 30)) * 1000
    val sixMonthAgo = (currentDate / 1000 - (60 * 60 * 24 * 180)) * 1000
    val yearAgo = (currentDate / 1000 - (60 * 60 * 24 * 365)) * 1000
    val dateStart = rememberSaveable { mutableLongStateOf(weerAgo) }
    val dateEnd = rememberSaveable { mutableLongStateOf(currentDate) }
    val openDialogPeriod = rememberSaveable { mutableStateOf(false) }
    val openDialogDate = rememberSaveable { mutableStateOf(false) }
    val stateDateRangePicker = rememberDateRangePickerState()
    val stateLabelPeriod = rememberSaveable { mutableStateOf("неделю") }
    val orders = ordersViewModel.orders.observeAsState(listOf())
    val ordersSort: List<Order> = orders.value.filter { order ->
        order.dateAdd >= dateStart.longValue && order.dateAdd <= dateEnd.longValue
    }.reversed()
    val countAllOrdersAsPeriod = ordersSort.size.toString()
    val data = mutableListOf<LineChartData.Point>().apply {
        if (ordersSort.isNotEmpty()) {
            for (order in ordersSort) {
                add(LineChartData.Point(order.priceWork.toFloat(), order.dateAdd.toString()))
            }
        }
    }
    val prizeZ = ordersSort.sumOf { it.priceZip   }
    val profit = ordersSort.sumOf { it.priceWork  }
    val countActiveOrders = ordersSort.count { it.isWork }
    val countClosedOrders = ordersSort.count { !it.isWork }

    Log.d("TEST_AnalyticsScreen", "Orders: $ordersSort")
    Log.d("TEST_AnalyticsScreen", "countAllOrders: $countAllOrdersAsPeriod")
    Log.d("TEST_AnalyticsScreen", "countActiveOrders: $countActiveOrders")
    Log.d("TEST_AnalyticsScreen", "countClosedOrders: $countClosedOrders")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Данные за",
                    Modifier.padding(end = 6.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                InputChip(
                    selected = true,
                    onClick = {
                        openDialogPeriod.value = true
                    },
                    label = {
                        Text(
                            stateLabelPeriod.value,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                )
            }
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    "${dateFormatter(dateStart.longValue)} - ${dateFormatter(dateEnd.longValue)}",
                    fontSize = 16.sp
                )
            }

            if (ordersSort.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Нет данных для отображения за указанный период. Выберите другой.",
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    Text("Количество заказов: $countAllOrdersAsPeriod", fontSize = 16.sp)
                    Text("Расходы на запчасти: $prizeZ руб.", fontSize = 16.sp)
                    Text("Прибыль: $profit руб.", fontSize = 16.sp)

                    // График запчасти и стоимость
                    Row(modifier = Modifier.fillMaxSize()) {
                        val modelProducer = remember { CartesianChartModelProducer() }
                        LaunchedEffect(Unit) {
                            withContext(Dispatchers.Default) {
                                while (isActive) {
                                    modelProducer.runTransaction {
                                        lineSeries {
                                            repeat(Defaults.MULTI_SERIES_COUNT) {
                                                series(
                                                    List(Defaults.ENTRY_COUNT) {
                                                        Defaults.COLUMN_LAYER_MIN_Y +
                                                                Random.nextFloat() * Defaults.COLUMN_LAYER_RELATIVE_MAX_Y
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    delay(Defaults.TRANSACTION_INTERVAL_MS)
                                }
                            }
                        }
                        ChartPrize(modelProducer = modelProducer, modifier = Modifier)
                    }
                    Row(
                        Modifier
                            .padding(vertical = 18.dp)
                            .fillMaxWidth()
                    ) {
                        HorizontalDivider(thickness = 1.dp, color = Color.Black)
                    }

                    // График количества заказов
                    Row(modifier = Modifier.fillMaxSize()) {
                        val modelProducer2 = remember { CartesianChartModelProducer() }
                        LaunchedEffect(Unit) {
                            withContext(Dispatchers.Default) {
                                while (isActive) {
                                    modelProducer2.runTransaction {
                                        columnSeries {
                                            repeat(3) {
                                                series(
                                                    List(1) {
                                                        Defaults.COLUMN_LAYER_MIN_Y +
                                                                Random.nextFloat() * Defaults.COLUMN_LAYER_RELATIVE_MAX_Y
                                                    }
                                                )
                                            }
                                        }
                                        lineSeries { series(List(1) { Random.nextFloat() * Defaults.MAX_Y }) }
                                    }
                                    delay(Defaults.TRANSACTION_INTERVAL_MS)
                                }
                            }
                        }
                        ChartCountOrders(
                            modelProducer = modelProducer2,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }

    // Выбор заданного периода
    if (openDialogPeriod.value) {
        Dialog(onDismissRequest = { openDialogPeriod.value = false }) {
            OutlinedCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ElevatedCard(
                        onClick = {
                            openDialogPeriod.value = false
                            stateLabelPeriod.value = "неделю"
                            dateStart.longValue = weerAgo
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Неделя", Modifier.align(Alignment.Center), fontSize = 18.sp)
                        }
                    }
                    ElevatedCard(
                        onClick = {
                            openDialogPeriod.value = false
                            stateLabelPeriod.value = "месяц"
                            dateStart.longValue = monthAgo
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) { Text("Месяц", Modifier.align(Alignment.Center), fontSize = 18.sp) }
                    }
                    ElevatedCard(
                        onClick = {
                            openDialogPeriod.value = false
                            stateLabelPeriod.value = "полгода"
                            dateStart.longValue = sixMonthAgo
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Полгода", Modifier.align(Alignment.Center), fontSize = 18.sp)
                        }
                    }
                    ElevatedCard(
                        onClick = {
                            openDialogPeriod.value = false
                            stateLabelPeriod.value = "год"
                            dateStart.longValue = yearAgo
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Год", Modifier.align(Alignment.Center), fontSize = 18.sp)
                        }
                    }
                    ElevatedCard(
                        onClick = {
                            openDialogPeriod.value = false
                            openDialogDate.value = true
                            stateLabelPeriod.value = "выбор периода"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                "Произвольный период",
                                Modifier.align(Alignment.Center),
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Выбор произвольного периода
    if (openDialogDate.value) {
        DatePickerDialog(
            onDismissRequest = { openDialogDate.value = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialogDate.value = false
                        if (stateDateRangePicker.selectedStartDateMillis != null && stateDateRangePicker.selectedEndDateMillis != null) {
                            dateStart.longValue = stateDateRangePicker.selectedStartDateMillis!!
                            dateEnd.longValue = stateDateRangePicker.selectedEndDateMillis!!
                            stateLabelPeriod.value = "период:"
                        } else {
                            stateLabelPeriod.value = "период не выбран"
                        }
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialogDate.value = false
                    stateLabelPeriod.value = "период не выбран"
                }) { Text("Отмена") }
            }
        ) {
            DateRangePicker(
                state = stateDateRangePicker,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            )
        }
    }

}

@Composable
fun ChartPrize(modelProducer: CartesianChartModelProducer, modifier: Modifier) {
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    chartColorsChartPrize.map { color ->
                        rememberLine(
                            fill = remember { LineCartesianLayer.LineFill.single(fill(color)) },
                            areaFill = null,
                        )
                    }
                )
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
            marker = rememberMarker(),
            legend = rememberLegendChartPrize(),
        ),
        modelProducer = modelProducer,
        modifier = modifier.height(280.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = true),
    )
}

@Composable
private fun rememberLegendChartPrize(): Legend<CartesianMeasuringContext, CartesianDrawingContext> {
    val lineNameList = listOf("Расходы на запчасти", "Стоимость заказа")
    val labelComponent = rememberTextComponent(vicoTheme.textColor)
    return rememberHorizontalLegend(
        items = rememberExtraLambda {
            chartColorsChartPrize.forEachIndexed { index, color ->
                add(
                    LegendItem(
                        icon = shapeComponent(color, Shape.Pill),
                        labelComponent = labelComponent,
                        label = lineNameList[index],
                    )
                )
            }
        },
        iconSize = 8.dp,
        iconPadding = 8.dp,
        spacing = 4.dp,
        padding = Dimensions.of(top = 8.dp)
    )
}

@Composable
private fun ChartCountOrders(modelProducer: CartesianChartModelProducer, modifier: Modifier) {
    val marker = rememberMarker()
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider =
                ColumnCartesianLayer.ColumnProvider.series(
                    columnColorsChartCountOrders.map { color ->
                        rememberLineComponent(color = color, thickness = 8.dp, shape = Shape.rounded(2.dp))
                    }
                )
            ),
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    rememberLine(
                        remember { LineCartesianLayer.LineFill.single(fill(Color(0xffa485e0))) }
                    )
                )
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(guideline = null),
            marker = marker,
            legend = rememberLegendChartCountOrders()
        ),
        modelProducer = modelProducer,
        modifier = modifier.height(280.dp),
        zoomState = rememberVicoZoomState(zoomEnabled = false),
    )
}

@Composable
private fun rememberLegendChartCountOrders(): Legend<CartesianMeasuringContext, CartesianDrawingContext> {
    val lineNameList = listOf("Всего", "Активные заказы", "Закрытые заказы")
    val labelComponent = rememberTextComponent(vicoTheme.textColor)
    return rememberHorizontalLegend(
        items = rememberExtraLambda {
            columnColorsChartCountOrders.forEachIndexed { index, color ->
                add(
                    LegendItem(
                        icon = shapeComponent(color, Shape.Pill),
                        labelComponent = labelComponent,
                        label = lineNameList[index],
                    )
                )
            }
        },
        iconSize = 8.dp,
        iconPadding = 8.dp,
        spacing = 4.dp,
        padding = Dimensions.of(top = 8.dp)
    )
}

private val chartColorsChartPrize = listOf(Color.Red, Color.Green)
private val columnColorsChartCountOrders = listOf(Color(0xff916cda), Color(0xffd877d8), Color(0xfff094bb))

@Composable
internal fun rememberMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = Shape.markerCornered(Corner.FullyRounded)
    val labelBackground =
        rememberShapeComponent(
            color = MaterialTheme.colorScheme.surfaceBright,
            shape = labelBackgroundShape,
            shadow =
            rememberShadow(
                radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP.dp,
                dy = LABEL_BACKGROUND_SHADOW_DY_DP.dp,
            ),
        )
    val label =
        rememberTextComponent(
            color = MaterialTheme.colorScheme.onSurface,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            padding = Dimensions.of(8.dp, 4.dp),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
        )
    val indicatorFrontComponent =
        rememberShapeComponent(MaterialTheme.colorScheme.surface, Shape.Pill)
    val indicatorCenterComponent = rememberShapeComponent(shape = Shape.Pill)
    val indicatorRearComponent = rememberShapeComponent(shape = Shape.Pill)
    val indicator =
        rememberLayeredComponent(
            rear = indicatorRearComponent,
            front =
            rememberLayeredComponent(
                rear = indicatorCenterComponent,
                front = indicatorFrontComponent,
                padding = Dimensions.of(5.dp),
            ),
            padding = Dimensions.of(10.dp),
        )
    val guideline = rememberAxisGuidelineComponent()
    return remember(label, labelPosition, indicator, showIndicator, guideline) {
        @SuppressLint("RestrictedApi")
        object :
            DefaultCartesianMarker(
                label = label,
                labelPosition = labelPosition,
                indicator =
                if (showIndicator) {
                    { color ->
                        LayeredComponent(
                            rear = ShapeComponent(color.copyColor(alpha = 0.15f), Shape.Pill),
                            front =
                            LayeredComponent(
                                rear =
                                ShapeComponent(
                                    color = color,
                                    shape = Shape.Pill,
                                    shadow = Shadow(radiusDp = 12f, color = color),
                                ),
                                front = indicatorFrontComponent,
                                padding = Dimensions.of(5.dp),
                            ),
                            padding = Dimensions.of(10.dp),
                        )
                    }
                } else {
                    null
                },
                indicatorSizeDp = 36f,
                guideline = guideline,
            ) {
            override fun updateInsets(
                context: CartesianMeasuringContext,
                horizontalDimensions: HorizontalDimensions,
                model: CartesianChartModel,
                insets: Insets,
            ) {
                with(context) {
                    val baseShadowInsetDp =
                        CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS_DP
                    var topInset = (baseShadowInsetDp - LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    var bottomInset = (baseShadowInsetDp + LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    when (labelPosition) {
                        LabelPosition.Top,
                        LabelPosition.AbovePoint -> topInset += label.getHeight(context) + tickSizeDp.pixels

                        LabelPosition.Bottom -> bottomInset += label.getHeight(context) + tickSizeDp.pixels
                        LabelPosition.AroundPoint -> {}
                    }
                    insets.ensureValuesAtLeast(top = topInset, bottom = bottomInset)
                }
            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f

object Defaults {
    const val TRANSACTION_INTERVAL_MS = 3_000L
    const val MULTI_SERIES_COUNT = 2
    const val ENTRY_COUNT = 30
    const val MAX_Y = 5_000
    const val COLUMN_LAYER_MIN_Y = 100
    const val COLUMN_LAYER_RELATIVE_MAX_Y = MAX_Y - COLUMN_LAYER_MIN_Y
}