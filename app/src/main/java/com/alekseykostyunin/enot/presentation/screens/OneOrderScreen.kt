package com.alekseykostyunin.enot.presentation.screens

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings.System.DATE_FORMAT
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alekseykostyunin.enot.App
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.navigation.NavigationItem
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.HistoryOrderViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.pushpal.jetlime.EventPointType
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneOrderScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel,
) {
    val historyOrderViewModel: HistoryOrderViewModel = viewModel()
    val orderLD = ordersViewModel.order.observeAsState()
    val order = orderLD.value

    //checkPermission()

    Scaffold(
//        floatingActionButtonPosition = FabPosition.End,
//
//        floatingActionButton = {
//            Column {
//                Button(
//                    onClick = {
//
//                    },
//                    elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
//                ) {
//                    Icon(
//                        Icons.Filled.Add,
//                        contentDescription = null,
//                    )
//                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//                    Text(text = "Добавить шаг")
//                }
//            }
//        },

        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
                    .padding(innerPadding),
            ) {
                Column {
                    val dt = DateUtil.dateFormatter(order?.dateAdd.toString())
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                navigationState.navigateTo(NavigationItem.AllOrders.route)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                        Text(
                            text = "Заказ от $dt",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(vertical = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = {
                                navigationState.navigateTo(NavigationItem.EditOrder.route)
                            },
                            modifier = Modifier.weight(1f)

                        ) {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = null
                            )
                        }
                    }

                    Column(

                    ) {
                        var state by remember { mutableIntStateOf(0) }
                        val titles = listOf("Описание", "Исполнение")
                        SecondaryTabRow(selectedTabIndex = state) {
                            titles.forEachIndexed { index, title ->
                                Tab(
                                    selected = state == index,
                                    onClick = { state = index },
                                    text = {
                                        Text(
                                            text = title,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 16.sp
                                        )
                                    }
                                )
                            }
                        }

                        if (state == 0) {
                            if (order != null) {
                                DescOrder(order)
                            }
                        } else {
                            HistoryOrder()
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun VerticalEventContent(item: HistoryStep, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            text = item.time,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 12.sp,
            text = item.text,
        )
        Row {
            //item.foto.forEach { it ->
                //val id = R.drawable.image_1
//                val link = "R.drawable.${it}" as Int
//                val context = LocalContext.current
//                val s = ContextCompat.getDrawable(
//                    context,
//                    link
//                )
//
//                Image(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .padding(8.dp)
//                        .clip(
//                            RoundedCornerShape(5),
//                        ),
//                    contentScale = ContentScale.Crop,
//                    painter = painterResource(id = link),
//                    contentDescription = null,
//                )
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                        .clip(
                            RoundedCornerShape(5),
                        ),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.image_2),
                    contentDescription = null,
                )
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                        .clip(
                            RoundedCornerShape(5),
                        ),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.image_1),
                    contentDescription = null,
                )
            //}
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun DescOrder(
    order: Order
) {
    Column(
//        modifier = Modifier.

    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp),

        ) {
            Text(
                text = "Статус: ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = if (order.isWork) "активный" else "закрытый",
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier.padding(top = 10.dp),
        ) {
            Text(
                text = "Клиент: ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = order.client.toString(),
                fontSize = 16.sp

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
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = order.description.toString(),
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier.padding(top = 10.dp),
        ) {
            Text(
                text = "Тип заказа: ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = order.type.toString(),
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier.padding(top = 10.dp),
        ) {
            Text(
                text = "Модель: ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = order.model.toString(),
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier.padding(top = 10.dp),
        ) {
            Text(
                text = "Цена запчастей: ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = order.priceZip.toString(),
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier.padding(top = 10.dp),
        ) {
            Text(
                text = "Стоимость заказа: ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = order.priceWork.toString(),
                fontSize = 16.sp
            )
        }


    }


}

@Composable
fun HistoryOrder() {
    // История
    val historyStep1 = HistoryStep(0, "03.03.2024", 2, "Заказ создан")
    val historyStep2 = HistoryStep(1, "04.03.2024", 2, "Заказал запчасти")
    val historyStep3 = HistoryStep(2, "05.03.2024", 2, "Закончил ремонт")
    val historyStep4 = HistoryStep(3, "06.03.2024", 2, "Отдал клиенту")
    val historyStep5 = HistoryStep(4, "06.03.2024", 3, "Заказ закрыт")
    val listHistory = remember {
        mutableListOf(
            historyStep1,
            historyStep2,
            historyStep3,
            historyStep4,
            historyStep5
        )
    }
    JetLimeColumn(
        modifier = Modifier.padding(top = 16.dp),
        itemsList = ItemsList(listHistory),
        key = { _, item -> item.id },
    ) { index, item, position ->
        JetLimeEvent(
            style = JetLimeEventDefaults.eventStyle(
                position = position,
                pointAnimation = if (item.type == 3) JetLimeEventDefaults.pointAnimation() else null,
                pointType = when (item.type) {
                    1 -> EventPointType.filled(0.8f)
                    3 -> EventPointType.custom(
                        icon = painterResource(id = R.drawable.icon_check),

                        )

                    else -> EventPointType.Default
                },
//                                pointStrokeWidth = when (index) {
//                                    3 -> 0.dp
//                                    else -> 2.dp
//                                },
            ),
        ) {
            VerticalEventContent(
                item = item,
            )

        }
    }
}
