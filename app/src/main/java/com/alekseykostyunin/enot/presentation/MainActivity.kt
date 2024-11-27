package com.alekseykostyunin.enot.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.repositoryimpl.ClientsRepositoryImpl
import com.alekseykostyunin.enot.data.repositoryimpl.OrdersRepositoryImpl
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.domain.repository.ClientsRepository
import com.alekseykostyunin.enot.domain.repository.OrdersRepository
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.clients.AddClientUseCase
import com.alekseykostyunin.enot.domain.usecase.clients.AllClientsUseCase
import com.alekseykostyunin.enot.domain.usecase.clients.EditClientUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AddHistoryStepUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AddOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AddPhotoOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AllOrdersUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.CloseOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.EditOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.users.AuthUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.RegUserUseCase
import com.alekseykostyunin.enot.domain.usecase.users.ResetPasswordUseCase
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase
import com.alekseykostyunin.enot.presentation.navigation.StartNavigation
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.UserViewModel
import com.alekseykostyunin.enot.presentation.viewmodelsfactory.ClientsViewModelFactory
import com.alekseykostyunin.enot.presentation.viewmodelsfactory.OrdersViewModelFactory
import com.alekseykostyunin.enot.presentation.viewmodelsfactory.UserViewModelFactory
import com.alekseykostyunin.enot.ui.theme.EnotTheme
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    /* Repository */
    private val userRepository: UsersRepository = UsersRepositoryImpl
    private val ordersRepository: OrdersRepository = OrdersRepositoryImpl
    private val clientsRepository: ClientsRepository = ClientsRepositoryImpl

    /* Use cases */

    /* Users */
    private val regUserUseCase: RegUserUseCase = RegUserUseCase(userRepository)
    private val authUserUseCase: AuthUserUseCase = AuthUserUseCase(userRepository)
    private val resetPasswordUseCase: ResetPasswordUseCase = ResetPasswordUseCase(userRepository)
    private val singOutUserUseCase: SingOutUserUseCase = SingOutUserUseCase(userRepository)

    /* Orders */
    private val allOrdersUseCase: AllOrdersUseCase = AllOrdersUseCase(ordersRepository)
    private val addOrderUseCase: AddOrderUseCase = AddOrderUseCase(ordersRepository)
    private val addPhotoOrderUseCase: AddPhotoOrderUseCase = AddPhotoOrderUseCase(ordersRepository)
    private val editOrderUseCase: EditOrderUseCase = EditOrderUseCase(ordersRepository)
    private val closeOrderUseCase: CloseOrderUseCase = CloseOrderUseCase(ordersRepository)
    private val addHistoryStepUseCase: AddHistoryStepUseCase =
        AddHistoryStepUseCase(ordersRepository)

    /* Clients */
    private val allClientsUseCase: AllClientsUseCase = AllClientsUseCase(clientsRepository)
    private val addClientUseCase: AddClientUseCase = AddClientUseCase(clientsRepository)
    private val editClientUseCase: EditClientUseCase = EditClientUseCase(clientsRepository)

    /* View models */
    private val userViewModel: UserViewModel by viewModels(
        factoryProducer = {
            UserViewModelFactory(
                regUserUseCase = regUserUseCase,
                authUserUseCase = authUserUseCase,
                resetPasswordUseCase = resetPasswordUseCase,
                singOutUserUseCase = singOutUserUseCase
            )
        }
    )

    private val ordersViewModel: OrdersViewModel by viewModels(
        factoryProducer = {
            OrdersViewModelFactory(
                allOrdersUseCase = allOrdersUseCase,
                addOrderUseCase = addOrderUseCase,
                addPhotoOrderUseCase = addPhotoOrderUseCase,
                editOrderUseCase = editOrderUseCase,
                closeOrderUseCase = closeOrderUseCase,
                addHistoryStepUseCase = addHistoryStepUseCase,
            )
        }
    )

    private val clientsViewModel: ClientsViewModel by viewModels(
        factoryProducer = {
            ClientsViewModelFactory(
                addClientUseCase = addClientUseCase,
                allClientsUseCase = allClientsUseCase,
                editClientUseCase = editClientUseCase,
            )
        }
    )


    /* Permissions */
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

            else -> launcherCallPhone.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private val getContact =
        registerForActivityResult(ActivityResultContracts.PickContact()) { uri: Uri? ->
            if (uri != null) {
                val contactProjection = arrayOf(
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
                )
                val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                val phoneProjection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
                val phone = mutableListOf<String>()
                contentResolver.query(
                    uri,
                    contactProjection,
                    null,
                    null,
                    null)?.use { cursor ->
                    val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val hasPhoneIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
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
                                    phone.add(phoneCursor.getString(numberIndex))
                                }
                            }
                        } else {
                            phone.add(getString(R.string.not_number_phone))
                        }
                        Log.i(
                            "TEST_contact",
                            "ID: $id, Name: $name, hasPhone: $hasPhoneIndex, Number: $phone"
                        )
                        clientsViewModel.addClient(name, phone)
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnotTheme {
                StartNavigation(
                    userViewModel = userViewModel,
                    ordersViewModel = ordersViewModel,
                    clientsViewModel = clientsViewModel,
                    requestCameraPermission = { requestCameraPermission() },
                    requestContactsPermission = { requestContactsPermission() },
                    requestCallPhonePermission = { requestCallPhonePermission() },
                    cameraExecutor = cameraExecutor,
                    getContact = { getContact.launch(null) },
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}