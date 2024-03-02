package com.alekseykostyunin.enot.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun UserScreen(
    onClickButtonSighOut: () -> Unit
) {
    val repository: UsersRepository = UsersRepositoryImpl
    val singOutUserUseCase = SingOutUserUseCase(repository)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            val auth: FirebaseAuth = Firebase.auth
            val user = auth.currentUser
            val email = user?.email.toString()
            val useruid = user?.uid.toString()
            Row {
                Text(
                    text = "E-mail: ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email,
                    fontSize = 24.sp,
                )
            }
            Row {
                Text(
                    text = "User uid: ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = useruid,
                    fontSize = 24.sp,
                )
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = {
                    singOutUserUseCase.singOutUser()
                    onClickButtonSighOut()
                }
            ) {
                Text(text = "Выйти из приложения")
            }

        }

    }
}