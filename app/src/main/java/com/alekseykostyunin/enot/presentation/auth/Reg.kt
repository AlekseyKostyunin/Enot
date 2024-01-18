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
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.presentation.view.Destinations
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//@Preview
@Composable
fun Reg(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Text(
            text = "Регистрация",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        val email = remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email.value,
            label = { Text("E-mail") },
            singleLine = true,
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

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            onClick = {
                //reg(email.value,password)
                Log.d("TEST_1", email.value) // fds@gfddh.fe
                Log.d("TEST_2", email.toString()) // MutableState(value=fds@gfddh.fe)@262410069
                navController.navigate(Destinations.Authorisation.route)




            }
        ) {
            Text(text = "Отправить")
        }
    }



}

private fun reg(email: String, password: String){
    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
//        .addOnCompleteListener(this) { task ->
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TEST_1", "createUserWithEmail:success")
                //val user = auth.currentUser
                //updateUI(user)
            } else {
                Log.w("TEST_1", "createUserWithEmail:failure", task.exception)
//                Toast.makeText(
//                   baseContext,
//                    "Authentication failed.",
//                    Toast.LENGTH_SHORT,
//                ).show()
                //updateUI(null)
            }
        }
}