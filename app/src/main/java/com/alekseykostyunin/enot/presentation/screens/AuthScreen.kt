package com.alekseykostyunin.enot.presentation.screens

import android.annotation.SuppressLint
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
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.alekseykostyunin.enot.presentation.viewmodels.MainViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
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
            text = stringResource(R.string.authorization),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        val email = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            label = { Text("E-mail") },
            onValueChange = { newText -> email.value = newText },
            singleLine = true,
        )

        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by rememberSaveable { mutableStateOf(false) }
        val icon = if (passwordVisibility) painterResource(R.drawable.design_ic_visibility)
        else painterResource(R.drawable.design_ic_visibility_off)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
                if (email.value.isEmpty()) {
                    sendToast(context.getString(R.string.error_email_not_empty))
                } else {
                    val isValidEmail = Validate.isEmailValid(email.value)
                    if (!isValidEmail) {
                        sendToast(context.getString(R.string.error_incorrect_email_try_again))
                    } else {
                        if (password.isEmpty()) {
                            sendToast(context.getString(R.string.error_passord_not_empty))
                        } else {
                            if (password.length < 6) {
                                sendToast(context.getString(R.string.error_password_is_short))
                            } else {
                                //mainViewModel.signInWithEmailAndPassword(email.value, password)

                                val auth: FirebaseAuth = Firebase.auth
                                auth.signInWithEmailAndPassword(email.value, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d("TEST_sign", "signInWithEmail:success")
                                            sendToast(context.getString(R.string.success_auth))
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
                                                    message = context.getString(R.string.error_invalid_login_or_password),
                                                    actionLabel = context.getString(R.string.close),
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