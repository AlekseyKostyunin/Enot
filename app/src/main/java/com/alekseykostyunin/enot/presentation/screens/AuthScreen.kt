package com.alekseykostyunin.enot.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.utils.Validate
import com.alekseykostyunin.enot.presentation.general.LogoAnimation
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.UserViewModel
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AuthScreen(
    navigationState: NavigationState,
    userViewModel: UserViewModel,
    ordersViewModel: OrdersViewModel,
    clientsViewModel: ClientsViewModel,
    snackBarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    fun sendToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoAnimation()
        Text(
            text = stringResource(R.string.authorization),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        var email by rememberSaveable { mutableStateOf("") }
        var isErrorEmail by rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
            isError = isErrorEmail,
            value = email,
            label = { Text("E-mail") },
            onValueChange = { newText -> email = newText },
            singleLine = true,
        )

        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by rememberSaveable { mutableStateOf(false) }
        val icon = if (passwordVisibility) painterResource(R.drawable.design_ic_visibility)
        else painterResource(R.drawable.design_ic_visibility_off)
        var isErrorPassword by rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                errorTextColor = Color.Red,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            isError = isErrorPassword,
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = icon,
                        contentDescription = "visibility icon"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation()
        )

        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            onClick = {
                if (email.isEmpty()) {
                    isErrorEmail = true
                    sendToast(context.getString(R.string.error_email_not_empty))
                } else {
                    val isValidEmail = Validate.isEmailValid(email)
                    if (!isValidEmail) {
                        isErrorEmail = true
                        sendToast(context.getString(R.string.error_incorrect_email_try_again))
                    } else {
                        if (password.isEmpty()) {
                            isErrorEmail = false
                            isErrorPassword = true
                            sendToast(context.getString(R.string.error_passord_not_empty))
                        } else {
                            if (password.length < 6) {
                                isErrorPassword = true
                                sendToast(context.getString(R.string.error_password_is_short))
                            } else {
                                isErrorPassword = false
                                userViewModel.auth(email, password) { success ->
                                    if (success) {
                                        sendToast(context.getString(R.string.success_auth))
                                        ordersViewModel.updateOrders()
                                        clientsViewModel.updateClients()
                                        userViewModel.setStatusAuthorized(true)
                                    } else {
                                        Log.d(
                                            "TEST_sign",
                                            "signInWithEmail:failure"
                                        )
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                message = context.getString(R.string.error_invalid_login_or_password),
                                                actionLabel = context.getString(R.string.close),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        ) {
            Text(stringResource(R.string.enter))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.recover_password),
                Modifier.clickable { navigationState.navigateTo(Destinations.ResetPassword.route) }
            )
            Text(
                stringResource(R.string.registration),
                Modifier.clickable { navigationState.navigateTo(Destinations.Registration.route) }
            )
        }
    }

}