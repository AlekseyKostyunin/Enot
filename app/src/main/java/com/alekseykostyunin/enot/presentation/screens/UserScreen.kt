package com.alekseykostyunin.enot.presentation.screens

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.Logout
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.sharp.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.ActivityNavigator
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.data.repositoryimpl.UsersRepositoryImpl
import com.alekseykostyunin.enot.data.utils.DateUtil
import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.UsersRepository
import com.alekseykostyunin.enot.domain.usecase.users.SingOutUserUseCase
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
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

@Composable
fun UserScreen2(
    onClickButtonSighOut: () -> Unit
) {
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
                    onClickButtonSighOut()
                }
            ) {
                Text(text = "Выйти из приложения")
            }

        }

    }
}

@Composable
fun UserScreen3(
    navigationState: NavigationState,
    onClickButtonSighOut: () -> Unit,
//    onGitHubClick: () -> Unit,
//    onPlaystoreClick: () -> Unit,
//    onFdroidClick: () -> Unit
) {
    val context = LocalContext.current
    var version = ""
    try {
        val pInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        version = pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    val openDialogSignOut = remember { mutableStateOf(false) }

    var toContact by rememberSaveable { mutableStateOf(false) }
    if (toContact) {
        ToContact(context)
        toContact = false
    }

    Box(Modifier.fillMaxSize()){
        Column(Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                //colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_enot),
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = Modifier
                                .size(140.dp)
                                .scale(1.5f)
                        )
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            text = stringResource(R.string.version, version),
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                        Text(
                            text = "Alexey Kostyunin",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {  },
                            modifier = Modifier
                                .size(70.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)

                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.github),
                                contentDescription = "GitHub",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        IconButton(
                            onClick = {  },
                            modifier = Modifier
                                .size(70.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)

                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.google_play),
                                contentDescription = "Playstore",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            // Политика конфиденциальности
            Row {
                ListItem(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            //navigationState.navigateTo(Destinations.PrivacyPolicy.route)
                            navigationState.navHostController.navigate(Destinations.PrivacyPolicy.route){
                                popUpTo(Destinations.User.route){
                                    saveState = true
                                }
                            }
                        },
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.privacy_policy),
                            style = MaterialTheme.typography.titleMedium
                        ) },
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.click_here_to_view_our_privacy_policy),
                            style = MaterialTheme.typography.bodySmall
                        ) },
                    trailingContent = {},
                    leadingContent = {
                        Icon(
                            Icons.Outlined.PrivacyTip,
                            contentDescription = stringResource(R.string.privacy_policy),
                            tint = MaterialTheme.colorScheme.primary
                        ) },
                    tonalElevation = 5.dp
                )
            }
            // Контакт
            Row {
                ListItem(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { toContact = true},
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.contact),
                            style = MaterialTheme.typography.titleMedium
                        ) },
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.contact_the_author_by_email),
                            style = MaterialTheme.typography.bodySmall
                        ) },
                    trailingContent = {},
                    leadingContent = {
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = stringResource(R.string.contact),
                            tint = MaterialTheme.colorScheme.primary
                        ) },
                    tonalElevation = 5.dp
                )
            }
            // Выход
            Row {
                ListItem(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { openDialogSignOut.value = true },
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.signOut),
                            style = MaterialTheme.typography.titleMedium
                        ) },
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.click_here_to_view_signOut),
                            style = MaterialTheme.typography.bodySmall
                        ) },
                    trailingContent = {},
                    leadingContent = {
                        Icon(
                            Icons.AutoMirrored.Sharp.Logout,
                            contentDescription = stringResource(R.string.privacy_policy),
                            tint = MaterialTheme.colorScheme.primary
                        ) },
                    tonalElevation = 5.dp
                )
            }
        }

    }

    if (openDialogSignOut.value) {
        Dialog(
            onDismissRequest = {
                openDialogSignOut.value = false
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Выйти из приложения?",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = {
                                openDialogSignOut.value = false
                            },
                            modifier = Modifier.padding(end = 8.dp),
                        ) {
                            Text("Отмена")
                        }
                        Button(
                            onClick = { onClickButtonSighOut() },
                            modifier = Modifier.padding(start = 8.dp),
                        ) {
                            Text("Выйти")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ToContact(context: Context) {
    val authorEmail = "kostunin-ag@mail.ru"
    val cc = ""
    val subject = "[Техническая поддержка] Приложение E-note"
    val bodyText = "О приложении E-note для Android:\n"
    val mailto = "mailto:" + Uri.encode(authorEmail) +
            "?cc=" + Uri.encode(cc) +
            "&subject=" + Uri.encode(subject) +
            "&body=" + Uri.encode(bodyText)

    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.setData(Uri.parse(mailto))
    startActivity(context, emailIntent, null)
}