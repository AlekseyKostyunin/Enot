package com.alekseykostyunin.enot.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun RegScreen(
    navigationState: NavigationState
) {
    val context = LocalContext.current
    fun sendToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_enot),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.reg),
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
            singleLine = true,
            onValueChange = { newText -> email.value = newText },
        )

        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        val icon = if (passwordVisibility) painterResource(id = R.drawable.design_ic_visibility)
        else painterResource(id = R.drawable.design_ic_visibility_off)
        var isErrorPassword by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
            isError = isErrorPassword,
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
        var checked by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it }
            )
            TextButton(
                onClick = {
                    navigationState.navHostController.navigate(Destinations.PrivacyPolicy.route) {
                        popUpTo(Destinations.Registration.route) {
                            saveState = true
                        }
                    }
                },
                content = { Text(stringResource(R.string.i_accept_the_terms)) }
            )
        }

        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            //enabled = enable.value,
            onClick = {
                if (email.value.isEmpty()) {
                    isErrorEmail = true
                    sendToast(context.getString(R.string.error_email_not_empty))
                } else {
                    val isValidEmail = Validate.isEmailValid(email.value)
                    if (!isValidEmail) {
                        isErrorEmail = true
                        sendToast(context.getString(R.string.error_incorrect_email_try_again))
                    } else {
                        if (password.isEmpty()) {
                            sendToast(context.getString(R.string.error_passord_not_empty))
                            isErrorEmail = false
                            isErrorPassword = true
                        } else {
                            if (password.length < 6) {
                                isErrorPassword = true
                                sendToast(context.getString(R.string.error_password_is_short))
                            } else {
                                isErrorPassword = false

                                if (!checked) {
                                    sendToast(context.getString(R.string.you_need_to_accept_the_terms_of_user_agreement))
                                } else {
                                    val auth: FirebaseAuth = Firebase.auth
                                    val database = Firebase.database.reference
                                    auth.createUserWithEmailAndPassword(email.value, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d("TEST_1", "createUserWithEmail:success")
                                                val user =
                                                    auth.currentUser ?: return@addOnCompleteListener
                                                //updateUI(user)
                                                val userId = user.uid
                                                database.child("users").child(userId).child("email")
                                                    .setValue(email.value)
                                                sendToast(context.getString(R.string.reg_succes))
                                            } else {
                                                Log.w(
                                                    "TEST_1",
                                                    "createUserWithEmail:failure",
                                                    task.exception
                                                )
                                                sendToast(context.getString(R.string.error_reg))
                                            }
                                        }
                                    Log.d("TEST_email_reg", email.value)
                                    Log.d("TEST_password_reg", password)
                                    navigationState.navigateTo(Destinations.Authorisation.route)

                                }


                            }
                        }
                    }
                }
            }
        ) {
            Text(stringResource(R.string.send))
        }
    }
}