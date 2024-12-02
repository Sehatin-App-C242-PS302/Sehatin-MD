package com.c242_ps302.sehatin.presentation.screen.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.data.local.entity.UserEntity
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.components.settings_item.Language
import com.c242_ps302.sehatin.presentation.components.settings_item.LanguageSettingsItem
import com.c242_ps302.sehatin.presentation.components.settings_item.SettingsItem
import com.c242_ps302.sehatin.presentation.components.settings_item.SwitchSettingsItem
import com.c242_ps302.sehatin.presentation.components.toast.SehatinToast
import com.c242_ps302.sehatin.presentation.components.toast.ToastType
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme
import com.c242_ps302.sehatin.presentation.utils.LanguageChangeHelper

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    languageChangeHelper: LanguageChangeHelper = hiltViewModel(),
    viewModel: SettingsViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit
) {
    val state by viewModel.settingsState.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state.error != null) {
            toastMessage = state.error ?: "Unknown error"
            toastType = ToastType.ERROR
            showToast = true
        } else if (state.success && state.user != null) {
            toastMessage = "User data loaded successfully!"
            toastType = ToastType.SUCCESS
            showToast = true
        }
    }

    val context = LocalContext.current
    val isDarkTheme = state.isDarkTheme
    val isNotificationEnabled = state.isNotificationEnabled

    val languagesList = listOf(
        Language("en", "English", R.drawable.uk_flag),
        Language("in", "Indonesia", R.drawable.indo_flag)
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.toggleNotification(true)
        } else {
            viewModel.toggleNotification(false)
        }
    }

    fun handleNotificationPermission(enable: Boolean) {
        if (!enable) {
            viewModel.toggleNotification(false)
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    viewModel.toggleNotification(true)
                }
                else -> {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            viewModel.toggleNotification(true)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(visible = state.isLoading) {
            CircularProgressIndicator()
        }

        AnimatedVisibility(visible = state.error != null) {
            Text(
                text = state.error ?: "Unknown error",
                color = MaterialTheme.colorScheme.error,
                maxLines = 2
            )
        }

        AnimatedVisibility(visible = !state.isLoading && state.error == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                SehatinAppBar()
                Spacer(modifier = Modifier.height(40.dp))
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person Icon",
                    modifier = Modifier
                        .padding(20.dp)
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.inversePrimary)
                )

                when (val userState = state.user) {
                    is UserEntity -> {
                        Text(
                            text = userState.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = userState.email,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    null -> {
                        Text(
                            text = "User  not found",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                SettingsItem(
                    leadingIcon = Icons.Default.Person,
                    text = stringResource(R.string.account),
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                SettingsItem(
                    leadingIcon = Icons.Default.Security,
                    text = stringResource(R.string.privacy_security),
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                SwitchSettingsItem(
                    leadingIcon = Icons.Default.DarkMode,
                    text = stringResource(R.string.dark_theme),
                    checked = isDarkTheme,
                    onCheckedChange = { viewModel.toggleDarkTheme() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                SwitchSettingsItem(
                    leadingIcon = Icons.Default.Notifications,
                    text = stringResource(R.string.notification),
                    checked = isNotificationEnabled,
                    onCheckedChange = { enabled -> handleNotificationPermission(enabled) }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                LanguageSettingsItem(
                    leadingIcon = Icons.Default.Language,
                    languagesList = languagesList,
                    text = stringResource(R.string.language),
                    onCurrentLanguageChange = { languageCode ->
                        languageChangeHelper.changeLanguage(context, languageCode)
                    },
                    languageChangeHelper = languageChangeHelper
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                SettingsItem(
                    leadingIcon = Icons.AutoMirrored.Filled.Logout,
                    text = stringResource(R.string.logout),
                    onClick = { viewModel.logout(onLogoutSuccess) }
                )
            }
        }

        if (showToast) {
            SehatinToast(
                message = toastMessage,
                type = toastType,
                duration = 2000L,
                onDismiss = { showToast = false }
            )
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SehatinTheme {
        SettingsScreen(
            onLogoutSuccess = {}
        )
    }
}