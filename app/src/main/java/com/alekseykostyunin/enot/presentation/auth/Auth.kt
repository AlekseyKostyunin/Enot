package com.alekseykostyunin.enot.presentation.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.presentation.menu.SetMenu
import com.alekseykostyunin.enot.presentation.view.Destinations
import com.alekseykostyunin.enot.presentation.view.NavigationGraph
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//@Preview
@Composable
fun Auth(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Авторизация",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        val email = remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            label = { Text("E-mail") },
            //placeholder = { Text("Введите логин") },
            onValueChange = { newText -> email.value = newText },
        )
        var password by rememberSaveable { mutableStateOf("")}
        var passwordVisibility by remember {mutableStateOf(false)}
        val icon = if (passwordVisibility) painterResource(id = R.drawable.design_ic_visibility)
        else painterResource(id = R.drawable.design_ic_visibility_off)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            //placeholder = { Text() },
            onValueChange = { password = it},
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
            visualTransformation = if(passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation()

        )
        //val navController2: NavHostController = rememberNavController()
        /* Кнопка "Войти" */
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            onClick = {
                //auth(email.value,password) // aaaa@aaaa.ru  1234567
                navController.navigate(Destinations.SetMenu.route)
            }
        ) {

            Text(text = "Войти")
        }
        /* Кнопка "Регистрация" */
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            onClick = {
                navController.navigate(Destinations.Registration.route)
            }
        ) {
            Text(text = "Регистрация")
        }
    }

}

private fun auth(email: String, password: String){
    val auth = Firebase.auth
    auth.signInWithEmailAndPassword(email, password)
//        .addOnCompleteListener(this) { task ->
        .addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TEST_sign", "signInWithEmail:success")
                val user = auth.currentUser
                //updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TEST_sign", "signInWithEmail:failure", task.exception)
//                Toast.makeText(
//                    baseContext,
//                    "Authentication failed.",
//                    Toast.LENGTH_SHORT,
//                ).show()
                //updateUI(null)
            }
        }
//        .addOnFailureListener(this){
        .addOnFailureListener(){
            Log.w("TEST_", it.toString())
//            Toast.makeText(
//                baseContext,
//                it.toString(),
//                Toast.LENGTH_SHORT,
//            ).show()
        }

}