package com.alekseykostyunin.enot.presentation.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pushpal.jetlime.EventPointType
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import java.io.File
import java.util.concurrent.ExecutorService

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneOrderScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel,
    requestCameraPermission: () -> Unit,
    requestCallPhonePermission: () -> Unit,
    cameraExecutor: ExecutorService,
) {
    val activity = LocalContext.current as Activity
    var state by remember { mutableIntStateOf(0) }
    val order = ordersViewModel.order.observeAsState().value
    val shouldShowCamera = remember { mutableStateOf(false) }
    val outputDirectory: File = getOutputDirectory(activity)

    fun handleImageCapture(uri: Uri) {
        Log.i("TEST_camera_handleImageCapture", "Image captured: $uri")
        ordersViewModel.addPhoto(uri.toString())
        shouldShowCamera.value = false
    }

    /* Диалог - Добавление шага исполнения */
    val openDialogStep = remember { mutableStateOf(false) }
    if (openDialogStep.value) {
        var descStep by remember { mutableStateOf("") }
        val isErrorDescStep by rememberSaveable { mutableStateOf(false) }
        Dialog(onDismissRequest = { openDialogStep.value = false }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Добавление шага исполнения",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    )
                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                        isError = isErrorDescStep,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        value = descStep,
                        label = { Text("Описание шага исполнения") },
                        onValueChange = { newText -> descStep = newText },
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = {
                                openDialogStep.value = false
                            },
                            modifier = Modifier.padding(horizontal = 8.dp),
                        ) {
                            Text("Отмена")
                        }

                        Button(
                            onClick = {
                                val auth: FirebaseAuth = Firebase.auth
                                val database = Firebase.database.reference
                                val user = auth.currentUser
                                if (user != null) {
                                    order?.let {
                                        val userId = user.uid
                                        val idOrder = order.id
                                        val dateNewStep = DateUtil.dateOfUnit
                                        val history = order.history?.toMutableList()
                                        history?.let {
                                            val oldHistoryStep = history.last().apply {
                                                type = 1
                                            }
                                            val odlIdHistoryStep = oldHistoryStep.id

                                            val newIdHistoryStep = odlIdHistoryStep.plus(1)
                                            val newHistoryStep = HistoryStep(
                                                newIdHistoryStep,
                                                dateNewStep,
                                                2,
                                                descStep
                                            )
                                            history.add(newHistoryStep)
                                            Log.d("TEST_history", history.toString())
                                        }

                                        val orderUpdate = Order(
                                            id = idOrder,
                                            client = order.client,
                                            dateAdd = order.dateAdd,
                                            dateClose = 0,
                                            description = order.description,
                                            type = order.type,
                                            model = order.model,
                                            priceZip = order.priceZip,
                                            priceWork = order.priceWork,
                                            isWork = true,
                                            history = history,
                                            photos = order.photos,
                                            comment = order.comment,
                                        )

                                        database
                                            .child("users")
                                            .child(userId)
                                            .child("orders")
                                            .child(idOrder!!)
                                            .setValue(orderUpdate)

                                        val dbNewOrderUpdate = database
                                            .child("users")
                                            .child(userId)
                                            .child("orders")
                                            .child(idOrder)

                                        dbNewOrderUpdate.addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                val order2 = snapshot.getValue(Order::class.java)
                                                if (order2 != null) {
                                                    Log.d(
                                                        "TEST_snapshot_OneOrderScreen",
                                                        order2.toString()
                                                    )
                                                    ordersViewModel.getOrderUser(order2)
                                                    openDialogStep.value = false
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                Log.d("TEST_snapshot_error", error.message)
                                            }
                                        })
                                        ordersViewModel.updateOrders()
                                    }
                                }
                            },
                            modifier = Modifier.padding(horizontal = 8.dp),
                        ) {
                            Text("Добавить")
                        }
                    }
                }
            }
        }
    }

    /* Диалог - Закрытие заказа */
    val openDialogCloseOrder = remember { mutableStateOf(false) }
    if (openDialogCloseOrder.value) {
        Dialog(
            onDismissRequest = {
                openDialogCloseOrder.value = false
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Закрыть заказ?",
                        Modifier.padding(bottom = 12.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Button(
                        onClick = {
                            val auth: FirebaseAuth = Firebase.auth
                            val database = Firebase.database.reference
                            val user = auth.currentUser
                            if (user != null) {
                                order?.let {
                                    val userId = user.uid
                                    val idOrder = order.id
                                    val dateCloseOrder = DateUtil.dateOfUnit
                                    val history = order.history?.toMutableList()
                                    history?.let {
                                        val oldHistoryStep = history.last().apply {
                                            type = 1
                                        }
                                        val odlIdHistoryStep = oldHistoryStep.id

                                        val newIdHistoryStep = odlIdHistoryStep.plus(1)
                                        val newHistoryStep = HistoryStep(
                                            newIdHistoryStep,
                                            dateCloseOrder,
                                            3,
                                            "Заказ выполнен"
                                        )
                                        history.add(newHistoryStep)
                                        Log.d("TEST_history", history.toString())
                                    }

                                    val orderUpdate = Order(
                                        id = idOrder,
                                        client = order.client,
                                        dateAdd = order.dateAdd,
                                        dateClose = dateCloseOrder,
                                        description = order.description,
                                        type = order.type,
                                        model = order.model,
                                        priceZip = order.priceZip,
                                        priceWork = order.priceWork,
                                        isWork = false,
                                        history = history,
                                        photos = order.photos,
                                        comment = order.comment,
                                    )

                                    database
                                        .child("users")
                                        .child(userId)
                                        .child("orders")
                                        .child(idOrder!!)
                                        .setValue(orderUpdate)

                                    val dbNewOrderUpdate = database
                                        .child("users")
                                        .child(userId)
                                        .child("orders")
                                        .child(idOrder)

                                    dbNewOrderUpdate.addValueEventListener(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val order2 = snapshot.getValue(Order::class.java)
                                            if (order2 != null) {
                                                Log.d(
                                                    "TEST_snapshot_CloseOrder",
                                                    order2.toString()
                                                )
                                                ordersViewModel.getOrderUser(order2)
                                                openDialogCloseOrder.value = false
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("TEST_snapshot_error", error.message)
                                        }
                                    }
                                    )
                                    ordersViewModel.updateOrders()
//                                        navigationState.navigateTo(Destinations.OneOrder.route)
                                }

                            }
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text("Да")
                    }
                    Button(
                        onClick = {
                            openDialogCloseOrder.value = false
                        },
                        Modifier.fillMaxWidth()
                    ) {
                        Text("Нет")
                    }
                }
            }
        }
    }

    /* Окно об отсутствии номера телефона для звонка */
    val openDialogNotNumberPhone = remember { mutableStateOf(false) }
    if (openDialogNotNumberPhone.value) {
        Dialog(
            onDismissRequest = {
                openDialogNotNumberPhone.value = false
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Отсутствует номер телефона",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = {
                                openDialogNotNumberPhone.value = false
                            },
                            modifier = Modifier.padding(end = 8.dp),
                        ) {
                            Text("Закрыть")
                        }
                    }
                }
            }
        }
    }

    /* Диалог выбор номера телефона */
    val openDialogSelectNumberPhone = remember { mutableStateOf(false) }
    if (openDialogSelectNumberPhone.value) {
        Dialog(
            onDismissRequest = {
                openDialogSelectNumberPhone.value = false
            }
        ) {
            OutlinedCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (order != null) {
                        Text(
                            text = "Позвонить по номеру",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        )
                        for (oneNumberPhone in order.client?.phone!!) {
                            ElevatedCard(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_CALL)
                                    intent.data = Uri.parse("tel:$oneNumberPhone")
                                    activity.startActivity(intent)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        oneNumberPhone,
                                        Modifier.align(Alignment.Center),
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val titles = listOf("Описание", "История", "Фото")
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (state == 0) {
                Column {
                    Button(
                        onClick = {
                            requestCallPhonePermission()
                            if (order != null) {
                                if (order.client?.phone != null) {
                                    if (order.client.phone!![0] == "нет номера телефона") {
                                        openDialogNotNumberPhone.value = true
                                    } else {
                                        openDialogSelectNumberPhone.value = true
                                    }
                                }
                            }
                        },
                        elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                    ) {
                        Icon(
                            Icons.Filled.Phone,
                            contentDescription = null,
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Позвонить клиенту")
                    }
                }
            } else if (state == 1) {
                if (order?.isWork == true) {
                    Column {
                        Button(
                            onClick = {
                                openDialogStep.value = true
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
                    }
                }
            } else if (state == 2) {
                Column {
                    if (!shouldShowCamera.value && order?.isWork == true) {
                        Button(
                            onClick = {
                                requestCameraPermission()
                                ordersViewModel.notShowBottomBar()
                                shouldShowCamera.value = true
                            },
                            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = null,
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = "Добавить фото")
                        }
                    }
                }
            }
        },
        content = { innerPadding ->
            if (shouldShowCamera.value) {
                CameraView(
                    outputDirectory = outputDirectory,
                    executor = cameraExecutor,
                    onImageCaptured = ::handleImageCapture,
                    onError = { Log.e("TEST_camera", "View error:", it) }
                )
                Log.i("TEST_CameraView1", "CameraView")
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .padding(innerPadding),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val dt = DateUtil.dateFormatter(order?.dateAdd.toString())
                            IconButton(
                                onClick = {
                                    navigationState.navigateTo(Destinations.AllOrders.route)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                            Text(
                                text = "Заказ от $dt",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 10.dp),
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = {
                                    if (order?.isWork == true) {
                                        navigationState.navigateTo(Destinations.EditOrder.route)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Create,
                                    contentDescription = null
                                )
                            }
                            val color = if (order?.isWork == true) Color.Red
                            else Color.Green
                            IconButton(
                                onClick = {
                                    if (order?.isWork == true) {
                                        openDialogCloseOrder.value = true
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = color
                                )
                            }
                        }

                        Column {
                            SecondaryTabRow(selectedTabIndex = state) {
                                titles.forEachIndexed { index, title ->
                                    Tab(
                                        selected = state == index,
                                        onClick = { state = index },
                                        text = {
                                            Text(
                                                text = title,
                                                maxLines = 1,
                                                fontSize = 16.sp
                                            )
                                        }
                                    )
                                }
                            }

                            if (order != null) {
                                when (state) {
                                    0 -> {
                                        DescOrder(order)
                                    }
                                    1 -> {
                                        HistoryOrder(order)
                                    }
                                    2 -> {
                                        PhotosOrder(order, ordersViewModel)
                                    }
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
fun DescOrder(order: Order) {
    Column {
        Row(modifier = Modifier.padding(top = 18.dp)) {
            Text(
                text = "Статус: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = if (order.isWork) "активный" else "закрытый",
                fontSize = 18.sp
            )
        }
        Row(modifier = Modifier.padding(top = 18.dp)) {
            Text(
                text = "Клиент: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = order.client?.name.toString(),
                fontSize = 18.sp
            )
        }
        Row(modifier = Modifier.padding(top = 18.dp)) {
            Text(
                text = "Описание: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = order.description.toString(),
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier.padding(top = 18.dp),
        ) {
            Text(
                text = "Тип заказа: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = order.type.toString(),
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier.padding(top = 18.dp),
        ) {
            Text(
                text = "Модель: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = order.model.toString(),
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier.padding(top = 18.dp),
        ) {
            Text(
                text = "Цена запчастей: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = order.priceZip.toString(),
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier.padding(top = 18.dp),
        ) {
            Text(
                text = "Стоимость заказа: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = order.priceWork.toString(),
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier.padding(top = 18.dp),
        ) {
            Text(
                text = "Комментарий: ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = order.comment.toString(),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun HistoryOrder(order: Order) {
    if (order.history == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Здесь будет отображаться история исполнения заказа",
                textAlign = TextAlign.Center
            )
        }
    } else {
        order.history?.let {
            val listHistory = it
            JetLimeColumn(
                modifier = Modifier.padding(top = 16.dp),
                itemsList = ItemsList(listHistory),
                key = { _, item -> item.id },
            ) { _, item, position ->
                JetLimeEvent(
                    style = JetLimeEventDefaults.eventStyle(
                        position = position,
                        pointAnimation = if (item.type == 2) JetLimeEventDefaults.pointAnimation() else null,
                        pointType = when (item.type) {
                            1 -> EventPointType.filled(0.8f)
                            3 -> EventPointType.custom(painterResource(R.drawable.icon_check))
                            else -> EventPointType.Default
                        },
                        pointStrokeWidth = when (item.type) {
                            3 -> 0.dp
                            else -> 2.dp
                        }
                    )
                ) { VerticalEventContent(item) }
            }
        }
    }
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
                .padding(12.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            text = DateUtil.dateFormatterHHmm(item.time)
        )
        item.text?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 14.sp,
                text = it,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun PhotosOrder(
    order: Order,
    ordersViewModel: OrdersViewModel
) {
    if (order.photos == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                "Здесь будут отображаться фотографии исполнения заказа",
                textAlign = TextAlign.Center
            )
        }
    } else {

        /* Full Photo */
        val openDialogFullPhoto = remember { mutableStateOf(false) }
        val uriFullPhoto = ordersViewModel.urlPhoto.observeAsState("").value
        if (openDialogFullPhoto.value) {
            Dialog(
                onDismissRequest = { openDialogFullPhoto.value = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                AsyncImage(
                    model = uriFullPhoto,
                    contentDescription = null,
                    modifier = Modifier.clickable { openDialogFullPhoto.value = false }
                )
            }
        }

        val photos = order.photos!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(
                        items = photos,
                        key = { it.url.toString() }
                    ) { photo ->
                        AsyncImage(
                            model = photo.url,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    ordersViewModel.insertUrlPhoto(photo.url.toString())
                                    openDialogFullPhoto.value = true
                                }
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}