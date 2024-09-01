package com.alekseykostyunin.enot.presentation.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.presentation.navigation.Destinations
import com.alekseykostyunin.enot.presentation.navigation.NavigationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    navigationState: NavigationState,
) {
    val scroll: ScrollState = rememberScrollState(0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.privacy_policy),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        //fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            //navigationState.navigateTo(Destinations.User.route)
                            navigationState.navHostController.popBackStack()
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { it
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.background(Color.White)
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            PrivacyNoticeText()
        }
    }
}

@Composable
fun PrivacyNoticeText() {
    SectionText(stringResource(R.string.privacy_intro_title), stringResource(R.string.privacy_intro_content, stringResource(R.string.app_name)))
    SectionText(stringResource(R.string.privacy_data_collection_title), stringResource(R.string.privacy_data_collection_content, stringResource(R.string.app_name)))
    SectionText(stringResource(R.string.privacy_information_usage_title), stringResource(R.string.privacy_information_usage_content, stringResource(R.string.app_name)))
    SectionText(stringResource(R.string.privacy_data_security_title), stringResource(R.string.privacy_data_security_content, stringResource(R.string.app_name)))
    SectionText(stringResource(R.string.privacy_third_party_services_title), stringResource(R.string.privacy_third_party_services_content, stringResource(R.string.app_name)))
    SectionText(stringResource(R.string.privacy_changes_notice_title), stringResource(R.string.privacy_changes_notice_content, stringResource(R.string.app_name)))
    SectionText(stringResource(R.string.privacy_contact_us_title), stringResource(R.string.privacy_contact_us_content, "kostunin-ag@mail.ru"))
}

@Composable
fun SectionText(title: String, content: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.primary,

        )
    Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}