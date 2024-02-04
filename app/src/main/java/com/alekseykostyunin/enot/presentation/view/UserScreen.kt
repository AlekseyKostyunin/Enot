package com.alekseykostyunin.enot.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.users.AuthUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun UserScreen(
    navController: NavHostController
) {
    val repository: UsersRepository = UsersRepositoryImpl
    val singOutUserUseCase = SingOutUserUseCase(repository)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            val auth: FirebaseAuth = Firebase.auth
            val user = auth.currentUser
            val email = user?.email.toString()
            val idProvider = user?.providerId.toString()
            val idTenant = user?.tenantId.toString()
            val info = user.toString()
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
                    text = "IdUser: provider: ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = idProvider,
                    fontSize = 24.sp,
                )
            }
            Row {
                Text(
                    text = "IdUser: tenant: ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = idTenant,
                    fontSize = 24.sp,
                )
            }
            Row {
                Text(
                    text = "User info: ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = info,
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

            //user.uid
            OutlinedButton(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                onClick = {
                    singOutUserUseCase.singOutUser()
                    //StartNavigation()
                    navController.navigate(Destinations.Authorisation.route)
                }
            ) {
                Text(text = "Выйти из приложения")
            }
        }

    }
}