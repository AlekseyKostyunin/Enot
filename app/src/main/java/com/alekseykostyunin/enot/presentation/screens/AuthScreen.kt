package com.alekseykostyunin.enot.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.utils.Validate
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.MainViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    navigationState: NavigationState,
    mainViewModel: MainViewModel,
    ordersViewModel: OrdersViewModel,
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
        Image(
            painter = painterResource(id = R.drawable.ic_enot),
            contentDescription = null
        )
        Text(
            text = "Авторизация",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        val email = remember { mutableStateOf("") }
        var isErrorEmail by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
            isError = isErrorEmail,
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            label = { Text("E-mail") },
            onValueChange = { newText -> email.value = newText },
        )
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by rememberSaveable { mutableStateOf(false) }
        val icon = if (passwordVisibility) painterResource(R.drawable.design_ic_visibility)
        else painterResource(R.drawable.design_ic_visibility_off)
        var isErrorPassword by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                errorTextColor = Color.Red,
                focusedTextColor = Color.Black
            ),
            isError = isErrorPassword,
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Пароль") },
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
        //val enable = remember { mutableStateOf(false) }
        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            //enabled = enable.value,
            onClick = {
                if (email.value.isEmpty()) {
                    isErrorEmail = true
                    scope.launch {
                        val result = snackBarHostState.showSnackbar(
                            message = "Поле e-mail не может быть пустым!",
                            actionLabel = "Закрыть",
                        )
                        when (result) {
                            SnackbarResult.Dismissed -> {
                                isErrorEmail = false
                                isErrorPassword = false
                            }

                            SnackbarResult.ActionPerformed -> {
                                isErrorEmail = false
                                isErrorPassword = false
                            }
                        }
                    }
                } else {
                    val isValidEmail = Validate.isEmailValid(email.value)
                    if (!isValidEmail) {
                        isErrorEmail = true
                        scope.launch {
                            val result = snackBarHostState.showSnackbar(
                                message = "Некорректный e-mail. Повторите попытку!",
                                actionLabel = "Закрыть",
                            )
                            when (result) {
                                SnackbarResult.Dismissed -> {
                                    isErrorEmail = false
                                    isErrorPassword = false
                                }

                                SnackbarResult.ActionPerformed -> {
                                    isErrorEmail = false
                                    isErrorPassword = false
                                }
                            }
                        }
                    } else {
                        if (password.isEmpty()) {
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = "Полe пароль не может быть пустым!",
                                    actionLabel = "Закрыть",
                                )
                                when (result) {
                                    SnackbarResult.Dismissed -> {
                                        isErrorEmail = false
                                        isErrorPassword = false
                                    }

                                    SnackbarResult.ActionPerformed -> {
                                        isErrorEmail = false
                                        isErrorPassword = false
                                    }
                                }
                            }
                            isErrorEmail = false
                            isErrorPassword = true
                        } else {
                            if (password.length < 6) {
                                isErrorPassword = true
                                scope.launch {
                                    val result = snackBarHostState.showSnackbar(
                                        message = "Пароль слишком короткий (менее 6 символов). Повторите попытку!",
                                        actionLabel = "Закрыть",
                                    )
                                    when (result) {
                                        SnackbarResult.Dismissed -> {
                                            isErrorEmail = false
                                            isErrorPassword = false
                                        }

                                        SnackbarResult.ActionPerformed -> {
                                            isErrorEmail = false
                                            isErrorPassword = false
                                        }
                                    }
                                }
                            } else {
                                isErrorPassword = false
                                //enable.value = true
                                val auth: FirebaseAuth = Firebase.auth
                                auth.signInWithEmailAndPassword(email.value, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d("TEST_sign", "signInWithEmail:success")
                                            sendToast("Удачная авторизация!")
                                            mainViewModel.successAuth()
                                            ordersViewModel.updateOrders()
                                        } else {
                                            Log.w(
                                                "TEST_sign",
                                                "signInWithEmail:failure",
                                                task.exception
                                            )
                                            scope.launch {
                                                snackBarHostState.showSnackbar(
                                                    message = "Неверный логин или пароль. Повторите попытку входа!",
                                                    actionLabel = "Закрыть",
                                                )
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        Log.w("TEST_addOnFailureListener", it.toString())
                                    }
                            }
                        }
                    }
                }
            }
        ) {
            Text("Войти")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Восстановить пароль",
                Modifier.clickable { navigationState.navigateTo(Destinations.ResetPassword.route) }
            )
            Text("Регистрация",
                Modifier.clickable { navigationState.navigateTo(Destinations.Registration.route) }
            )
        }
    }
}