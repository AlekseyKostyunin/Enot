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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.firebase.MyFirebaseAuth
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.data.utils.Validate
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.users.ResetPasswordUseCase
import com.alekseykostyunin.enot.presentation.view.Destinations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ResetPassword(navController: NavController) {
    val context = LocalContext.current
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
            text = "Восстановление пароля",
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
        val repository: UsersRepository = UsersRepositoryImpl
        val resetPasswordUseCase = ResetPasswordUseCase(repository)

        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            onClick = {

                if (email.value.isEmpty()){
                    isErrorEmail = true
                    sendToast("Поле e-mail не может быть пустым!", context)
                }
                else {
                    val isValidEmail = Validate.isEmailValid(email.value)
                    if (!isValidEmail){
                        isErrorEmail = true
                        sendToast("Некорректный e-mail. Повторите попытку!", context)
                    }
                    else {
                        isErrorEmail = false
                        val auth: FirebaseAuth = Firebase.auth
                        auth.sendPasswordResetEmail(email.value)
                            .addOnSuccessListener {
                                Log.d("TEST_1", "yes" + it.toString())
                                sendToast("Письмо с инструкцией направлено на указанный e-mail.", context)
                                navController.navigate(Destinations.Authorisation.route)
                            }.addOnFailureListener {
                                Log.d("TEST_1", "not" + it.message)
                                sendToast("Неизвестная ошибка. Обратитесь в техподдержку.", context)
                                navController.navigate(Destinations.Authorisation.route)
                            }
                    }
                }
//                if (email.value.isEmpty()) sendToast("Поле e-mail не может быть пустым!",context)
//                if(!email.value.isEmailValid()) sendToast("Некорректный e-mail. Повторите попытку",context)
//                resetPasswordUseCase.resetPasswordUser(email.value)
//                // проверка наличия почты в базе
//                navController.navigate(Destinations.Authorisation.route)
            }
        ) {
            Text(text = "Восстановить пароль")
        }

    }
}

private fun sendToast(message: String, context: Context){
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG,
    ).show()
}