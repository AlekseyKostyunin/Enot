package com.alekseykostyunin.enot.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel

@Composable
fun EditClientScreen(
    navigationState: NavigationState,
    clientsViewModel: ClientsViewModel
) {
    val client = clientsViewModel.client.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navigationState.navigateTo(Destinations.OneClientAllOrders.route)
                }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(R.string.edit_client),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            var nameClient by remember { mutableStateOf(client.name ?: "") }
            val isErrorNameClient by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorNameClient,
                modifier = Modifier.fillMaxWidth(),
                value = nameClient,
                label = { Text(stringResource(R.string.name_client)) },
                onValueChange = { newText -> nameClient = newText }
            )

            var phoneClient by remember { mutableStateOf(client.phone?.joinToString(", ") ?: "") }
            val isErrorPhoneClient by rememberSaveable { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
                isError = isErrorPhoneClient,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = phoneClient,
                label = { Text(stringResource(R.string.phones_client)) },
                onValueChange = { newText -> phoneClient = newText }
            )

            Text(stringResource(R.string.phone_separated), Modifier.padding(vertical = 10.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = {
                    clientsViewModel.editClient(nameClient, phoneClient)
                    navigationState.navigateTo(Destinations.OneClientAllOrders.route)
                }
            ) {
                Text(text = stringResource(R.string.save_changes))
            }
        }

    }

}