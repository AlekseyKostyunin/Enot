package com.alekseykostyunin.enot.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alekseykostyunin.enot.presentation.auth.Auth
import com.alekseykostyunin.enot.presentation.auth.Reg
import com.alekseykostyunin.enot.presentation.menu.SetMenu
import com.alekseykostyunin.enot.presentation.view.NavigationGraph
import com.alekseykostyunin.enot.presentation.view.NavigationGraphAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val user: FirebaseUser? = auth.currentUser

        setContent {
            navController = rememberNavController()
            NavigationGraphAuth(navController)
            //Auth(navController)
//            if (user == null) {
//                Log.d("TEST_1", "not")
//                Auth(navController)
//            } else {
//                Log.d("TEST_1", user.uid)
//                SetMenu(navController)
//            }
        }
    }
}