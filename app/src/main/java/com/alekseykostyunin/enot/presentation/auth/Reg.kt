package com.alekseykostyunin.enot.presentation.auth

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.users.RegUserUseCase
import com.alekseykostyunin.enot.presentation.view.Destinations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Reg(
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
//        Image(
//            painter = painterResource(id = R.drawable.ic_enot),
//            contentDescription = null
//        )
        Text(
            text = "Регистрация",
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

        val repository: UsersRepository = UsersRepositoryImpl
        val regUserUseCase = RegUserUseCase(repository)

        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            onClick = {
                if (email.value.isEmpty()) {
                    isErrorEmail = true
                    sendToast("Поле e-mail не может быть пустым!", context)
                }
                else {
                    if (!email.value.isEmailValid()) {
                        isErrorEmail = true
                        sendToast("Некорректный e-mail. Повторите попытку", context)
                    }
                    else {
                        if (password.isEmpty()) {
                            sendToast("Полe пароль не может быть пустым!", context)
                            isErrorEmail = false
                            isErrorPassword = true
                        }
                        else {
                            if (password.length < 6) {
                                isErrorPassword = true
                                sendToast("Пароль слишком короткий (менее 6 символов). Повторите попытку!",context)
                            }
                            else {
                                isErrorPassword = false
                                val auth: FirebaseAuth = Firebase.auth
                                auth.createUserWithEmailAndPassword(email.value, password)
                                    .addOnCompleteListener() { task ->
                                        if (task.isSuccessful) {
                                            Log.d("TEST_1", "createUserWithEmail:success")
                                            val user = auth.currentUser
                                            //updateUI(user)
                                            sendToast(
                                                "Регистрация прошла успешно!",
                                                context
                                            )
                                            navController.navigate(Destinations.Authorisation.route)
                                        } else {
                                            Log.w(
                                                "TEST_1",
                                                "createUserWithEmail:failure",
                                                task.exception
                                            )
                                            //updateUI(null)
                                            sendToast(
                                                "Ошибка при регистрации. Обратитесь в техподдержку.",
                                                context
                                            )
                                            navController.navigate(Destinations.Authorisation.route)
                                        }
                                    }
                            }
                        }
                    }
                }

//                regUserUseCase.regUser(email.value,password)
//                Log.d("TEST_email_reg", email.value)
//                Log.d("TEST_password_reg", password)
//                navController.navigate(Destinations.Authorisation.route)
            }
        ) {
            Text(text = "Отправить")
        }
    }
}

private fun sendToast(message: String, context: Context) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG,
    ).show()
}

private fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}