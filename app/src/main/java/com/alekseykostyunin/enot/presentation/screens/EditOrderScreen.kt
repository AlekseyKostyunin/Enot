package com.alekseykostyunin.enot.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.domain.entities.Client
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOrderScreen(
    navigationState: NavigationState,
    ordersViewModel: OrdersViewModel
) {
    val order = ordersViewModel.order.observeAsState().value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Row {
                IconButton(onClick = {
                    navigationState.navigateTo(Destinations.OneOrder.route)
                }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(R.string.edit_order),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            var nameClient by remember { mutableStateOf(order?.client?.name) }
            val isErrorClient by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorClient,
                modifier = Modifier.fillMaxWidth(),
                value = nameClient ?: "",
                label = { Text(stringResource(R.string.client)) },
                onValueChange = { newText -> nameClient = newText },
            )

            var desc by remember { mutableStateOf(order?.description) }
            val isErrorDesc by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorDesc,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = desc ?: "",
                label = { Text(stringResource(R.string.desc_order)) },
                onValueChange = { newText -> desc = newText },
            )

            val options =
                listOf(stringResource(R.string.mobile_phone),
                    stringResource(R.string.computer),
                    stringResource(R.string.nootbook),
                    stringResource(R.string.television),
                    stringResource(R.string.tablet),
                    stringResource(R.string.other))
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(order?.type) }
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(top = 10.dp),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedOptionText ?: "",
                    onValueChange = { },
                    label = { Text(stringResource(R.string.type_order)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(text = selectionOption)
                            },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            var model by remember { mutableStateOf(order?.model) }
            val isErrorModel by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorModel,
                    modifier = Modifier.fillMaxWidth(),
                    value = model ?: "",
                    label = { Text(stringResource(R.string.model)) },
                    onValueChange = { newText -> model = newText },
                )
            }

            var priceZ by remember { mutableStateOf(order?.priceZip.toString()) }
            val isErrorPriceZ by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorPriceZ,
                    modifier = Modifier.fillMaxWidth(),
                    value = priceZ,
                    label = { Text(stringResource(R.string.price_zip)) },
                    onValueChange = { newText -> priceZ = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            var price by remember { mutableStateOf(order?.priceWork.toString()) }
            val isErrorPrice by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorPrice,
                    modifier = Modifier.fillMaxWidth(),
                    value = price,
                    label = { Text(stringResource(R.string.price_order2)) },
                    onValueChange = { newText -> price = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            var comment by remember { mutableStateOf(order?.comment ?: "") }
            val isErrorComment by rememberSaveable { mutableStateOf(false) }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                    isError = isErrorComment,
                    modifier = Modifier.fillMaxWidth(),
                    value = comment,
                    label = { Text(stringResource(R.string.comment)) },
                    onValueChange = { newText -> comment = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = {

                    val auth: FirebaseAuth = Firebase.auth
                    val database = Firebase.database.reference
                    val user = auth.currentUser
                    val editClient = Client(
                        id = order?.client?.id,
                        name = nameClient,
                        phone = order?.client?.phone
                    )
                    if (user != null) {
                        val userId = user.uid
                        val idOrder = order?.id
                        idOrder?.let {
                            val orderUpdate = Order(
                                id = idOrder,
                                client = editClient,
                                dateAdd = order.dateAdd,
                                dateClose = 0,
                                description = desc,
                                type = selectedOptionText,
                                model = model,
                                priceZip = priceZ.toInt(),
                                priceWork = price.toInt(),
                                isWork = true,
                                history = order.history,
                                photos = order.photos,
                                comment = comment
                            )

                            database
                                .child("users")
                                .child(userId)
                                .child("orders")
                                .child(idOrder)
                                .setValue(orderUpdate)

                            val dbNewOrderUpdate = database
                                .child("users")
                                .child(userId)
                                .child("orders")
                                .child(idOrder)

                            dbNewOrderUpdate.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val order2 = snapshot.getValue(Order::class.java)
                                    if (order2 != null) {
                                        Log.d("TEST_snapshot_EditOrderScreen", order2.toString())
                                        ordersViewModel.getOrderUser(order2)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("TEST_snapshot_error", error.message)
                                }
                            })
                            ordersViewModel.updateOrders()
                            navigationState.navigateTo(Destinations.OneOrder.route)
                        }

                    }
                }
            ) {
                Text(stringResource(R.string.save_changes))
            }
        }
    }
}