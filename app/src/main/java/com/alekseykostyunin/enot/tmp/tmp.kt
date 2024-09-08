package com.alekseykostyunin.enot.tmp

//@Composable
//fun NavGraphNotMenu(
//    navController: NavHostController,
//    authScreenContent: @Composable () -> Unit,
//    regScreenContent: @Composable () -> Unit,
//    resetScreenContent: @Composable () -> Unit,
//){
//    NavHost(
//        navController = navController,
//        startDestination = Destinations.Authorisation.route
//    ){
//        composable(Destinations.Authorisation.route) {
//            authScreenContent()
//        }
//        composable(Destinations.Registration.route) {
//            regScreenContent()
//        }
//        composable(Destinations.ResetPassword.route) {
//            resetScreenContent()
//        }
//    }
//}
//
//@Composable
//fun NavGraphWithMenu(
//    navController: NavHostController,
//
//    allOrdersScreenContent: @Composable () -> Unit,
//    addOrderScreenContent: @Composable () -> Unit,
//    oneOrderScreenContent: @Composable () -> Unit,
//    editOrderScreenContent: @Composable () -> Unit,
//
//    clientsScreenContent: @Composable () -> Unit,
//    analyticsScreenContent: @Composable () -> Unit,
//    userScreenContent: @Composable () -> Unit,
//){
//    NavHost(
//        navController = navController,
//        startDestination = NavigationItem.Orders.route
//    ){
//        navigation(
//            route = NavigationItem.Orders.route,
//            startDestination = NavigationItem.AllOrders.route
//        ){
//            composable(NavigationItem.AllOrders.route){
//                allOrdersScreenContent()
//            }
//            composable(NavigationItem.AddOrder.route){
//                addOrderScreenContent()
//            }
//            composable(NavigationItem.OneOrder.route){
//                oneOrderScreenContent()
//            }
//            composable(NavigationItem.EditOrder.route){
//                editOrderScreenContent()
//            }
//        }
//        composable(NavigationItem.Clients.route) {
//            clientsScreenContent()
//        }
//        composable(NavigationItem.Analytics.route) {
//            analyticsScreenContent()
//        }
//        composable(NavigationItem.User.route) {
//            userScreenContent()
//        }
//    }
//}

//sealed class Destinations(val route: String) {
//    data object Authorisation : Destinations(
//        route = ROUTE_AUTHORISATION_SCREEN
//    )
//    data object Registration : Destinations(
//        route = ROUTE_REGISTRATION_SCREEN
//    )
//    data object ResetPassword : Destinations(
//        route = ROUTE_RESET_PASSWORD_SCREEN
//    )
//    private companion object{
//        const val ROUTE_AUTHORISATION_SCREEN = "authorisation_screen"
//        const val ROUTE_REGISTRATION_SCREEN  = "registration_screen"
//        const val ROUTE_RESET_PASSWORD_SCREEN = "reset_screen"
//    }
//}

//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.AccountCircle
//import androidx.compose.material.icons.outlined.Home
//import androidx.compose.material.icons.outlined.Settings
//import androidx.compose.material.icons.outlined.Star
//import androidx.compose.ui.graphics.vector.ImageVector
//
//sealed class NavigationItem(
//    val route: String,
//    val title: String? = null,
//    val icon: ImageVector? = null
//) {
//    data object Orders : NavigationItem(
//        route = ROUTE_ORDERS_SCREEN,
//        title = "Заказы",
//        icon = Icons.Outlined.Home
//    )
//
//    data object Clients : NavigationItem(
//        route = ROUTE_CLIENTS_SCREEN,
//        title = "Клиенты",
//        icon = Icons.Outlined.AccountCircle
//    )
//
//    data object Analytics : NavigationItem(
//        route = ROUTE_ANALYTICS_SCREEN,
//        title = "Финансы",
//        icon = Icons.Outlined.Star
//    )
//
//    data object User : NavigationItem(
//        route = ROUTE_USER_SCREEN,
//        title = "Профиль",
//        icon = Icons.Outlined.Settings
//    )
//
//    data object AllOrders : NavigationItem(
//        route = ROUTE_ALL_ORDERS_SCREEN
//    )
//
//    data object AddOrder : NavigationItem(
//        route = ROUTE_ADD_ORDERS_SCREEN
//    )
//
//    data object OneOrder : NavigationItem(
//        route = ROUTE_ONE_ORDER_SCREEN
//    )
//
//    data object EditOrder : NavigationItem(
//        route = ROUTE_EDIT_ORDER_SCREEN
//    )
//
//    private companion object{
//        const val ROUTE_ORDERS_SCREEN = "orders_screen"
//        const val ROUTE_CLIENTS_SCREEN = "clients_screen"
//        const val ROUTE_ANALYTICS_SCREEN = "analytics_screen"
//        const val ROUTE_USER_SCREEN = "user_screen"
//        const val ROUTE_ALL_ORDERS_SCREEN = "all_orders_screen"
//        const val ROUTE_ADD_ORDERS_SCREEN = "add_order_screen"
//        const val ROUTE_ONE_ORDER_SCREEN = "one_order_screen"
//        const val ROUTE_EDIT_ORDER_SCREEN = "edit_order_screen"
//    }
//}


//sealed class StartScreenState {
//    data object NotAuthScreenState : StartScreenState()
//    data object AuthScreenState : StartScreenState()
//}

//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Badge
//import androidx.compose.material3.BadgedBox
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavDestination.Companion.hierarchy
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.currentBackStackEntryAsState
//import com.alekseykostyunin.enot.presentation.screens.AddOrderScreen
//import com.alekseykostyunin.enot.presentation.screens.AllOrdersScreen
//import com.alekseykostyunin.enot.presentation.screens.AnalyticsScreen
//import com.alekseykostyunin.enot.presentation.screens.ClientsScreen
//import com.alekseykostyunin.enot.presentation.screens.EditOrderScreen
//import com.alekseykostyunin.enot.presentation.screens.OneOrderScreen
//import com.alekseykostyunin.enot.presentation.screens.UserScreen
//import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
//import com.alekseykostyunin.enot.presentation.viewmodels.StartViewModel

//@Composable
//fun StartNavigation(
//    checkPermission: Unit
//) {
//    val startViewModel: StartViewModel = viewModel()
//    val screenState = startViewModel.startScreenState.observeAsState(StartScreenState.NotAuthScreenState)
//    when (screenState.value) {
//        is StartScreenState.NotAuthScreenState -> {
//            NotAuthUserState(startViewModel)
//        }
//
//        is StartScreenState.AuthScreenState -> {
//            AuthUserState(startViewModel)
//        }
//
//        else -> {
//            NotAuthUserState(startViewModel)
//        }
//    }
//}

//@Composable
//fun NotAuthUserState(startViewModel: StartViewModel) {
//    val navigationState = rememberNavigationState()
//    Scaffold { paddingValues ->
//        Box(
//            modifier = Modifier
//                .padding(paddingValues)
//                .background(Color.White),
//        ) {
//            NavGraphNotMenu(navigationState.navHostController,
//                authScreenContent = {
//                    AuthScreen(
//                        navigationState,
//                        startViewModel
//                    )
//                },
//                regScreenContent = {
//                    RegScreen(navigationState)
//                },
//                resetScreenContent = {
//                    ResetPasswordScreen(navigationState)
//                }
//            )
//        }
//    }
//}

//@Composable
//fun AuthUserState(startViewModel: StartViewModel) {
//    val navigationState = rememberNavigationState()
//    val ordersViewModel: OrdersViewModel = viewModel()
//    val isShowBottomBar = ordersViewModel.isShowBottomBar.observeAsState()
//    Scaffold(
//        bottomBar = {
//            if (isShowBottomBar.value == true) {
//                BottomBar(
//                    navController = navigationState.navHostController,
//                    ordersViewModel
//                )
//            }
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .padding(paddingValues)
//                .background(Color.White),
//        ) {
//            NavGraphWithMenu(
//                navigationState.navHostController,
//                allOrdersScreenContent = {
//                    AllOrdersScreen(
//                        navigationState,
//                        ordersViewModel
//                    )
//
//                },
//                addOrderScreenContent = {
//                    AddOrderScreen(
//                        navigationState,
//                        ordersViewModel
//                    )
//                },
//                oneOrderScreenContent = {
//                    OneOrderScreen(
//                        navigationState,
//                        ordersViewModel
//                    )
//                },
//                editOrderScreenContent = {
//                    EditOrderScreen(
//                        navigationState,
//                        ordersViewModel
//                    )
//                },
//                clientsScreenContent = { ClientsScreen() },
//                analyticsScreenContent = {
//                    AnalyticsScreen(
//                        ordersViewModel
//                    )
//                },
//                userScreenContent = {
//                    UserScreen(
//                        onClickButtonSighOut = { startViewModel.signOut() }
//                    )
//                }
//            )
//
//        }
//    }
//}
//
//@Composable
//fun BottomBar(
//    navController: NavHostController,
//    ordersViewModel: OrdersViewModel
//) {
//    val countActiveOrders = ordersViewModel.countActiveOrders.observeAsState()
//    val screens = listOf(
//        NavigationItem.Orders,
//        NavigationItem.Clients,
//        NavigationItem.Analytics,
//        NavigationItem.User
//    )
//    NavigationBar(
//        containerColor = Color.White
//    ) {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        //val currentRoute = navBackStackEntry?.destination?.route // получаем ссылку на текущий экран
//        screens.forEach { screen ->
//            val selected = navBackStackEntry?.destination?.hierarchy?.any {
//                it.route == screen.route
//            } ?: false
//
//            NavigationBarItem(
//                label = { Text(text = screen.title!!) },
//                icon = {
//
//                    if (screen.route == NavigationItem.Orders.route){
//                        BadgedBox(
//                            badge = {
//                                Badge(
//                                    containerColor = Color.Red,
//                                    contentColor = Color.White
//                                ) {
//                                    val badgeNumber = countActiveOrders.value.toString()
//                                    Text(
//                                        badgeNumber,
//                                        modifier = Modifier.semantics {
//                                            contentDescription = "$badgeNumber new notifications"
//                                        }
//
//                                    )
//
//                                }
//                            }) {
//                            Icon(
//                                imageVector = screen.icon!!,
//                                contentDescription = ""
//                            )
//                        }
//                    } else{
//                        Icon(
//                            imageVector = screen.icon!!,
//                            contentDescription = ""
//                        )
//                    }
//
//                },
//                selected = selected,
//                onClick = {
//                    navController.navigate(screen.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {// будут удалены все экраны до стартового
////                        popUpTo(navController.graph.startDestinationId) {// будут удалены все экраны до стартового
//                            saveState =
//                                true // при удалении экранов из бекстека их стейт будет сохранен
//                        }
//                        launchSingleTop =
//                            true // хранить только верхний последний стейт экрана, не хранить дублирование
//                        restoreState =
//                            true // при возрате на этот экран восстановить стейт этого экрана
//                    }
//                },
//                colors = NavigationBarItemDefaults.colors(
//                    unselectedTextColor = Color.Gray,
//                    selectedTextColor = Color.Black,
//                    unselectedIconColor = Color.Gray,
//                    selectedIconColor = Color.Black
//                ),
//
//                alwaysShowLabel = false
//            )
//
//        }
//    }
//}

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.alekseykostyunin.enot.data.firebase.MyFirebaseAuth
//import com.alekseykostyunin.enot.presentation.navigation.StartScreenState

//class StartViewModel : ViewModel() {
//
//    private val initialState = isUserAuth()
//    private var _startScreenState = MutableLiveData(initialState)
//    var startScreenState: LiveData<StartScreenState> = _startScreenState
//
//    private fun isUserAuth() : StartScreenState {
//        return if(!MyFirebaseAuth.currentUser()) {
//            StartScreenState.AuthScreenState
//        } else StartScreenState.NotAuthScreenState
//    }
//
//    fun successAuth(){
//        _startScreenState.value = StartScreenState.AuthScreenState
//    }
//
//    fun signOut(){
//        _startScreenState.value = StartScreenState.NotAuthScreenState
//    }
//
//}

//@Composable
//fun AuthScreen(
//    navigationState: NavigationState,
//    startViewModel: StartViewModel
//) {
//    val context = LocalContext.current
//    fun sendToast(message: String) {Toast.makeText(context, message, Toast.LENGTH_LONG,).show()}
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(40.dp)
//            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_enot),
//            contentDescription = null
//        )
//        Text(
//            text = "Авторизация",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//        )
//        val email = remember { mutableStateOf("") }
//        var isErrorEmail by rememberSaveable { mutableStateOf(false) }
//        OutlinedTextField(
//            colors = OutlinedTextFieldDefaults.colors(errorTextColor = Color.Red),
//            isError = isErrorEmail,
//            modifier = Modifier.fillMaxWidth(),
//            value = email.value,
//            label = { Text("E-mail") },
//            onValueChange = { newText -> email.value = newText },
//        )
//        var password by rememberSaveable { mutableStateOf("") }
//        var passwordVisibility by rememberSaveable { mutableStateOf(false) }
//        val icon = if (passwordVisibility) painterResource(id = R.drawable.design_ic_visibility)
//        else painterResource(id = R.drawable.design_ic_visibility_off)
//        var isErrorPassword by rememberSaveable { mutableStateOf(false) }
//        OutlinedTextField(
//            colors = OutlinedTextFieldDefaults.colors(
//                errorTextColor = Color.Red,
//                focusedTextColor = Color.Black),
//            isError = isErrorPassword,
//            modifier = Modifier.fillMaxWidth(),
//            value = password,
//            onValueChange = { password = it },
//            singleLine = true,
//            label = { Text("Пароль") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            trailingIcon = {
//                IconButton(onClick = {
//                    passwordVisibility = !passwordVisibility
//                }) {
//                    Icon(
//                        painter = icon,
//                        contentDescription = "visibility icon"
//                    )
//                }
//            },
//            visualTransformation = if (passwordVisibility) VisualTransformation.None
//            else PasswordVisualTransformation()
//        )
//
//        ElevatedButton(modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 10.dp),
//            onClick = {
//                if (email.value.isEmpty()) {
//                    isErrorEmail = true
//                    sendToast("Поле e-mail не может быть пустым!")
//                }
//                else {
//                    val isValidEmail = Validate.isEmailValid(email.value)
//                    if (!isValidEmail) {
//                        isErrorEmail = true
//                        sendToast("Некорректный e-mail. Повторите попытку!")
//                    }
//                    else {
//                        if (password.isEmpty()) {
//                            sendToast("Полe пароль не может быть пустым!")
//                            isErrorEmail = false
//                            isErrorPassword = true
//                        }
//                        else {
//                            if (password.length < 6) {
//                                isErrorPassword = true
//                                sendToast(
//                                    "Пароль слишком короткий (менее 6 символов). Повторите попытку!"
//                                )
//                            }
//                            else {
//                                isErrorPassword = false
//                                val auth: FirebaseAuth = Firebase.auth
//                                auth.signInWithEmailAndPassword(email.value, password)
//                                    .addOnCompleteListener { task ->
//                                        if (task.isSuccessful) {
//                                            Log.d("TEST_sign", "signInWithEmail:success")
//                                            sendToast("Удачная авторизация!")
//                                            startViewModel.successAuth()
//                                        } else {
//                                            Log.w(
//                                                "TEST_sign",
//                                                "signInWithEmail:failure",
//                                                task.exception
//                                            )
//                                            sendToast(
//                                                "Неверный логин или пароль. Повторите попытку входа!"
//                                            )
////                                            navController.navigate((Destinations.Authorisation.route))
//                                            navigationState.navigateTo((Destinations.Authorisation.route))
//                                        }
//                                    }
//                                    .addOnFailureListener {
//                                        Log.w("TEST_", it.toString())
//                                    }
//                            }
//                        }
//                    }
//
//                }
//
//
//            }
//        ) {
//            Text(text = "Войти")
//        }
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "Восстановить пароль",
//                modifier = Modifier.clickable {
//                    navigationState.navigateTo(Destinations.ResetPassword.route)
//                }
//            )
//            Text(
//                text = "Регистрация",
//                modifier = Modifier.clickable {
//                    navigationState.navigateTo(Destinations.Registration.route)
//                }
//            )
//        }
//    }
//}

//    private fun takePhoto2() {
//        val imageCapture = imageCapture ?: return
//
//        val contentValues = ContentValues().apply {
//            //put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//        }
//
//        val outputOption = ImageCapture.OutputFileOptions
//            .Builder(
//                this.contentResolver,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                contentValues
//            ).build()
//
//        imageCapture.takePicture(
//            outputOption,
//            executor,
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Photo failed: ${exc.message}", Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Saved: ${output.savedUri}", Toast.LENGTH_SHORT
//                    ).show()
//                    //viewModel.insertPhoto(output.savedUri.toString(), date)
//                }
//            }
//        )
//    }

//private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider
//            .getInstance(this)
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//            val preview = Preview.Builder().build()
//            //preview.setSurfaceProvider(binding.viewFinder.surfaceProvider) // устанавливаем SurfaceView
//            imageCapture = ImageCapture.Builder().build()
//            cameraProvider.unbindAll() // отключаем камеру
//            cameraProvider.bindToLifecycle(
//                this,
//                CameraSelector.DEFAULT_BACK_CAMERA,
//                //preview,
//                imageCapture
//            )
//        }, executor)
//    }

//private val launcher = registerForActivityResult(
//    ActivityResultContracts.RequestMultiplePermissions()
//) { map ->
//    if (map.values.all { it }) {
//        //startCamera()
//        //getContacts()
//    } else {
//        Toast.makeText(this, "Разрешения не получены!", Toast.LENGTH_SHORT).show()
//    }
//}

//private fun checkPermission() {
//    val isAllGranted = REQUEST_PERMISSIONS.all { permission ->
//        ContextCompat.checkSelfPermission(
//            this, permission
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//    if (isAllGranted) {
//        //startCamera()
//        //Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show()
//    } else {
//        launcher.launch(REQUEST_PERMISSIONS)
//    }
//}

/*
*     companion object {
        private val REQUEST_PERMISSIONS = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            add(Manifest.permission.READ_CONTACTS)
        }.toTypedArray()
    }
* */

//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
//        ActivityCompat.startActivityForResult(this, intent, 1, null)

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode != Activity.RESULT_OK) return
//        if (requestCode === 1 && data != null) {
//           val contactData: Uri? = data.data
//            val cursor: Cursor = managedQuery(contactData, null, null, null, null)
//            cursor.moveToFirst()
//            val number: String =
//                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
//            val name: String =
//                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
//            val id = cursor.getInt(
//                cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
//            )
//            Log.i("TEST_contact", "Name: $name, Number: $number, ID: $id")
//        }
//
//    }

//private fun getContact2() {
//    val contentUri = ContactsContract.Contacts.CONTENT_URI
//    val contactProjection =arrayOf(
//        ContactsContract.Contacts._ID,
//        ContactsContract.Contacts.DISPLAY_NAME,
//        ContactsContract.Contacts.HAS_PHONE_NUMBER // флаг наличия номера телефона
//    )
//
//    val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//    val phoneProjection = arrayOf(
//        ContactsContract.CommonDataKinds.Phone.NUMBER
//    )
//    val phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
//
//    val stringBuilder = StringBuilder()
//    val listContacts = mutableListOf<Client>()
//
//    val cursor = contentResolver.query(
//        contentUri, contactProjection, null, null, null
//    )?.use { cursor ->
//        val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
//        val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
//        val hasPhoneIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
//
//        var i = 0
//
//        while (cursor.moveToNext()) {
//            if (i == 20) break
//            stringBuilder.append(cursor.getString(nameIndex))
//                .append(": ")
//            val name = cursor.getString(nameIndex)
//            var number = " "
//            val hasPhone = cursor.getInt(hasPhoneIndex) > 0
//            if (hasPhone) {
//                val contactId =cursor.getString(idIndex)
//                contentResolver.query(
//                    phoneUri,
//                    phoneProjection,
//                    phoneSelection,
//                    arrayOf(contactId),
//                    null
//                )?.use {
//                        phoneCursor ->
//                    val numberIndex = phoneCursor.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Phone.NUMBER
//                    )
//
//                    while (phoneCursor.moveToNext()) {
//                        stringBuilder.append(phoneCursor.getString(numberIndex))
//                        number = number + phoneCursor.getString(numberIndex) + "; "
//                        stringBuilder.append(", ")
//
//                    }
//                }
//            } else{
//                stringBuilder.append("no phone number")
//            }
//            stringBuilder.append(";\n")
//            listContacts.add(Client(name = name, phone = number))
//            i++
//        }
//    }
//    println(stringBuilder.toString())
//}

//            Row {
//                var selectedIndex by remember { mutableStateOf(0) }
//                val options = listOf("Заказы", "Клиенты")
//                SingleChoiceSegmentedButtonRow(
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    options.forEachIndexed { index, label ->
//                        SegmentedButton(
//                            shape = SegmentedButtonDefaults.itemShape(
//                                index = index,
//                                count = options.size
//                            ),
//                            onClick = { selectedIndex = index },
//                            selected = index == selectedIndex
//                        ) {
//                            Text(text = label)
//                        }
//                    }
//                }
//            }

//            Box(
//                modifier = Modifier
//                    .padding(top = 16.dp)
//                    .height(250.dp)
//                //.width(350.dp),
//                ,
//                contentAlignment = Alignment.Center,
//
//                ) {
//                if(ordersSort.isEmpty()) {
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) { Text("Выберите период для отображения данных",
//                        textAlign = TextAlign.Center ) }
//                } else {
//                    Chart(data)
//                }
//
//            }
//            Row(Modifier.padding(top = 18.dp).fillMaxWidth()) {
//                HorizontalDivider(thickness = 1.dp, color = Color.Black)
//            }

//@Composable
//fun Chart(data: List<LineChartData.Point>) {
//
//    LineChart(
//        lineChartData = LineChartData(points = data),
//        modifier = Modifier
//            .fillMaxSize()
//            .border(BorderStroke(width = 1.dp, color = Color.Red)),
//        animation = simpleChartAnimation(),
//        pointDrawer = FilledCircularPointDrawer(color = MaterialTheme.colorScheme.primary),
//        lineDrawer = SolidLineDrawer(color = PurpleGrey80),
//        xAxisDrawer = SimpleXAxisDrawer(
//            //axisLabelFormatter = { dateFormatterHHmm(it.toString()) }
//            axisLabelFormatter = { it -> "1" }
//        ),
//        yAxisDrawer = SimpleYAxisDrawer(
//            labelValueFormatter = { it.toInt().toString() }
//        ),
//        horizontalOffset = 3f,
//    )
//}
//
//private fun randomYValue(): Float = Random.Default.nextInt(500, 1000).toFloat()

//            Row(
//                modifier = Modifier
//                    .padding(top = 18.dp)
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceAround,
//            ) {
//                Text(
//                    text = "Начальная дата",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//                Text(
//                    text = "Конечная дата",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//            }

//            Row(
//                Modifier
//                    .padding(vertical = 18.dp)
//                    .fillMaxWidth()
//            ) {
//                HorizontalDivider(thickness = 1.dp, color = Color.Black)
//            }


//            Row(Modifier.padding(vertical = 10.dp)) {
//                SingleChoiceSegmentedButtonRow(Modifier.fillMaxSize()) {
//                    options.forEachIndexed { index, label ->
//                        SegmentedButton(
//                            shape = SegmentedButtonDefaults.itemShape(
//                                index = index,
//                                count = options.size
//                            ),
//                            onClick = {
//                                selectedIndex = index
//                                when (index) {
//                                    0 -> {
//                                        dateStart.longValue =
//                                            (currentDate / 1000 - (60 * 60 * 24)) * 1000
//                                        dateEnd.longValue = currentDate
//                                        Log.d(
//                                            "TEST_dateS",
//                                            "TEST_dateS: ${dateFormatterHHmm(dateStart.longValue.toString())}"
//                                        )
//                                        Log.d(
//                                            "TEST_dateE",
//                                            "TEST_dateE: ${dateFormatterHHmm(dateEnd.longValue.toString())}"
//                                        )
//                                    }
//
//                                    1 -> { // Месяц
//                                        dateStart.longValue =
//                                            (currentDate / 1000 - (60 * 60 * 24 * 30)) * 1000
//                                        dateEnd.longValue = currentDate
//                                        Log.d(
//                                            "TEST_dateS",
//                                            "TEST_dateS: ${dateFormatterHHmm(dateStart.longValue.toString())}"
//                                        )
//                                        Log.d(
//                                            "TEST_dateE",
//                                            "TEST_dateE: ${dateFormatterHHmm(dateEnd.longValue.toString())}"
//                                        )
//                                    }
//
//                                    2 -> { // Квартал
//                                        dateStart.longValue =
//                                            (currentDate / 1000 - (60 * 60 * 24 * 30 * 3)) * 1000
//                                        dateEnd.longValue = currentDate
//                                        Log.d(
//                                            "TEST_dateS",
//                                            "TEST_dateS: ${dateFormatterHHmm(dateStart.longValue.toString())}"
//                                        )
//                                        Log.d(
//                                            "TEST_dateE",
//                                            "TEST_dateE: ${dateFormatterHHmm(dateEnd.longValue.toString())}"
//                                        )
//                                    }
//
//                                    3 -> { // Полгода
//                                        dateStart.longValue =
//                                            (currentDate / 1000 - (60 * 60 * 24 * 30 * 6)) * 1000
//                                        dateEnd.longValue = currentDate
//                                        Log.d(
//                                            "TEST_dateS",
//                                            "TEST_dateS: ${dateFormatterHHmm(dateStart.longValue.toString())}"
//                                        )
//                                        Log.d(
//                                            "TEST_dateE",
//                                            "TEST_dateE: ${dateFormatterHHmm(dateEnd.longValue.toString())}"
//                                        )
//                                    }
//
//                                    4 -> { // Год
//                                        dateStart.longValue =
//                                            (currentDate / 1000 - (60 * 60 * 24 * 365)) * 1000
//                                        dateEnd.longValue = currentDate
//                                        Log.d(
//                                            "TEST_dateS",
//                                            "TEST_dateS: ${dateFormatterHHmm(dateStart.longValue.toString())}"
//                                        )
//                                        Log.d(
//                                            "TEST_dateE",
//                                            "TEST_dateE: ${dateFormatterHHmm(dateEnd.longValue.toString())}"
//                                        )
//                                    }
//                                }
//                            },
//                            selected = index == selectedIndex
//                        ) {
//                            Text(
//                                text = label,
//                                fontSize = 10.sp
//                            )
//                        }
//                    }
//                }
//            }
//
//var selectedIndex by remember { mutableIntStateOf(-1) }
//val options = listOf("Сегодня", "Месяц", "Квартал", "Полгода", "Год")

//        if (openDialogDateEnd.value) {
////        val confirmEnabled = remember {
////            derivedStateOf { datePickerStateEnd.selectedDateMillis != null }
////        }
//            DatePickerDialog(
//                onDismissRequest = { openDialogDateEnd.value = false },
//                confirmButton = {
//                    TextButton(
//                        onClick = {
//                            openDialogDateEnd.value = false
//                            Log.d(
//                                "DatePickerDialogEnd",
//                                "TEST_dateE: ${datePickerStateEnd.selectedDateMillis}"
//                            )
//                        },
//                        //enabled = confirmEnabled.value
//                    ) {
//                        Text("OK")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { openDialogDateEnd.value = false }) { Text("Отмена") }
//                }
//            ) {
//                DatePicker(state = datePickerStateEnd)
//            }
//        }