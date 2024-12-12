package com.c242_ps302.sehatin.ui.screen.settings

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
import com.c242_ps302.sehatin.ui.components.dialog.CustomDialog
import com.c242_ps302.sehatin.ui.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.ui.components.settings_item.Language
import com.c242_ps302.sehatin.ui.components.settings_item.LanguageSettingsItem
import com.c242_ps302.sehatin.ui.components.settings_item.SettingsItem
import com.c242_ps302.sehatin.ui.components.settings_item.SwitchSettingsItem
import com.c242_ps302.sehatin.ui.components.toast.SehatinToast
import com.c242_ps302.sehatin.ui.components.toast.ToastType
import com.c242_ps302.sehatin.ui.theme.SehatinTheme
import com.c242_ps302.sehatin.ui.utils.LanguageChangeHelper

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    languageChangeHelper: LanguageChangeHelper = hiltViewModel(),
    onLogoutSuccess: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.settingsState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state.error != null && state.error != context.getString(R.string.user_not_found)) {
            toastMessage = state.error ?: context.getString(R.string.unknown_error)
            toastType = ToastType.ERROR
            showToast = true
            viewModel.clearError()
        } else if (!state.isLoading && state.success && state.user != null) {
            toastMessage = context.getString(R.string.user_data_loaded_successfully)
            toastType = ToastType.SUCCESS
            showToast = true
            viewModel.clearSuccess()
        }
    }

    LaunchedEffect(state.user) {
        viewModel.getUserData()
    }

    val isDarkTheme = state.isDarkTheme

    val languagesList = listOf(
        Language(stringResource(R.string.en), stringResource(R.string.english), R.drawable.uk_flag),
        Language(stringResource(R.string.in_code), stringResource(R.string.indonesia), R.drawable.indo_flag)
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.setNotificationsEnabled(true, context)
        } else {
            toastMessage = context.getString(R.string.notification_permission_denied)
            toastType = ToastType.ERROR
            showToast = true
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
                text = state.error ?: stringResource(R.string.unknown_error),
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
                    contentDescription = stringResource(R.string.person_icon),
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
                            text = stringResource(R.string.user_not_found),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                SettingsItem(
                    leadingIcon = Icons.Default.Person,
                    text = stringResource(R.string.account),
                    onClick = { onProfileClick() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
//                SettingsItem(
//                    leadingIcon = Icons.Default.Security,
//                    text = stringResource(R.string.privacy_security),
//                )
//                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
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
                    checked = notificationsEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED -> {
                                        viewModel.setNotificationsEnabled(true, context)
                                    }
                                    else -> {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                }
                            } else {
                                viewModel.setNotificationsEnabled(true, context)
                            }
                        } else {
                            viewModel.setNotificationsEnabled(false, context)
                        }
                    }
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
                    onClick = {
                        showLogoutDialog = true
                    }
                )
            }
        }

        if (showLogoutDialog) {
            CustomDialog(
                dialogTitle = stringResource(R.string.logout_confirmation),
                dialogMessage = stringResource(R.string.are_you_sure_you_want_to_logout),
                iconColor = MaterialTheme.colorScheme.error,
                confirmButtonColor = MaterialTheme.colorScheme.error,
                onConfirm = {
                    viewModel.logout {
                        onLogoutSuccess()
                        showLogoutDialog = false
                    }
                },
                onDismiss = {
                    showLogoutDialog = false
                }
            )
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
            onLogoutSuccess = {},
            onProfileClick = {}
        )
    }
}