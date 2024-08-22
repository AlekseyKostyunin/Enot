package com.alekseykostyunin.enot.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.alekseykostyunin.enot.presentation.MainActivity

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen() {
    var contactName by mutableStateOf("")
    var contactNumber by mutableStateOf("")



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Row {
                var selectedIndex by remember { mutableStateOf(0) }
                val options = listOf("Последние", "По алфавиту")
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxSize()
                ) {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex
                        ) {
                            Text(label)
                        }
                    }
                }
            }
//            for (i in 0 until 15) {
//                GetOneClient(number = i)
//            }
            ContactPicker(
                context = LocalContext.current, contactName, contactNumber
            )
        }
    }
}

@Composable
fun GetOneClient(number: Int) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(0.1.dp, Color.Black),
        modifier = Modifier
            //.size(width = 340.dp, height = 100.dp)
            .fillMaxSize()
            .padding(vertical = 10.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Фамилия Имя Отчество № $number",

            )
    }
}

@Composable
fun ContactPicker(
    context: Context,
    contactName: String,
    contactNumber: String,
) {
   val activity = LocalContext.current as Activity
    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // on below line creating a text for contact name
        Text(text = contactName, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // on below line adding a spacer
        Spacer(modifier = Modifier.height(20.dp))

        // on below line creating a text for contact number.
        Text(text = contactNumber, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // on below line adding a spacer
        Spacer(modifier = Modifier.height(20.dp))

        // on below line creating a button to pick contact.
        Button(
            // on below line adding on click for button.
            onClick = {
                // on below line checking if permission is granted.
                if (hasContactPermission(context)) {
                    // if permission granted open intent to pick contact/
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    startActivityForResult(activity, intent, 1, null)
                } else {
                    // if permission not granted requesting permission .
                    requestContactPermission(context, activity)
                }
            },
            // adding padding to button.
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // displaying text in our button.
            Text(text = "Pick Contact")

        }
    }
}

fun hasContactPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED;
}

fun requestContactPermission(context: Context, activity: Activity) {
    if (!hasContactPermission(context)) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), 1)
    }
}


