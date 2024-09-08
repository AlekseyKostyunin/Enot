package com.alekseykostyunin.enot.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alekseykostyunin.enot.presentation.navigation.StartNavigation
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.MainViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {

    private val mainViewModel : MainViewModel by viewModels()
    val ordersViewModel: OrdersViewModel by viewModels()
    val clientsViewModel: ClientsViewModel by viewModels()
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("TEST_camera_permission", "Permission granted")
        } else {
            Log.d("TEST_camera_permission", "Permission denied")
        }
    }

    private val launcherContact = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("TEST_contacts_permission", "Permission granted")
        } else {
            Log.d("TEST_contacts_permission", "Permission denied")
        }
    }

    private val launcherCallPhone = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("TEST_call_phone_permission", "Permission granted")
        } else {
            Log.d("TEST_call_phone_permission", "Permission denied")
        }
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("TEST_camera_permission", "Permission camera previously granted")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> Log.i("TEST_camera_permission", "Show camera permissions dialog")

            else -> launcherCamera.launch(Manifest.permission.CAMERA)
        }
    }

    private fun requestContactsPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("TEST_contact_permission", "Permission read contacts previously granted")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            ) -> Log.i("TEST_contact_permission", "Permission read contacts previously not granted")

            else -> launcherContact.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun requestCallPhonePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("TEST_call_phone_permission", "Permission call phone previously granted")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CALL_PHONE
            ) -> Log.i("EST_call_phone_permission", "Permission call phone previously not granted")

            else -> launcherContact.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private val getContact = registerForActivityResult(ActivityResultContracts.PickContact()) { uri: Uri? ->
        if (uri != null) {
            val contactProjection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
            )
            val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val phoneProjection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
            var numbers = ""
            contentResolver.query(uri, contactProjection, null, null, null)?.use {
                    cursor ->
                val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val hasPhoneIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                if (cursor.moveToFirst()) {
                    val id = cursor.getString(idIndex)
                    val name = cursor.getString(nameIndex)
                    val hasPhone = cursor.getInt(hasPhoneIndex) > 0
                    if (hasPhone) {
                        val contactId = cursor.getString(idIndex)
                        contentResolver.query(
                            phoneUri,
                            phoneProjection,
                            phoneSelection,
                            arrayOf(contactId),
                            null
                        )?.use { phoneCursor ->
                            val numberIndex = phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                            while (phoneCursor.moveToNext()) {
                                numbers = numbers + phoneCursor.getString(numberIndex) + " "
                            }
                        }
                    } else {
                        numbers = "нет номера телефона"
                    }
                    Log.i("TEST_contact", "ID: $id, Name: $name, hasPhone: $hasPhoneIndex, Number: $numbers")
                    clientsViewModel.addClient(id, name, numbers)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            StartNavigation(
                mainViewModel,
                ordersViewModel,
                clientsViewModel,
                { requestCameraPermission() },
                { requestContactsPermission() },
                { requestCallPhonePermission() },
                cameraExecutor,
                getContact = { getContact.launch(null) },
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @SuppressLint("Range")
    fun getContactNameAndPhone(context: Context, contactUri: Uri) {
        val cursor = context.contentResolver.query(contactUri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))

                val phones = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null)

                phones?.use {
                    while (it.moveToNext()) {
                        val phoneNumber = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        Log.d("Contact", "Name: $name, Phone: $phoneNumber")
                    }
                }
            }
        }
    }

}