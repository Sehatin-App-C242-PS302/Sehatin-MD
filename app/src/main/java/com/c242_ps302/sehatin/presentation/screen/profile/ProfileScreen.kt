package com.c242_ps302.sehatin.presentation.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.dialog.CustomDialog
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.components.toast.SehatinToast
import com.c242_ps302.sehatin.presentation.components.toast.ToastType
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.profileState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var name by remember { mutableStateOf(state.name ?: "") }
    var email by remember { mutableStateOf(state.email ?: "") }

    LaunchedEffect(state) {
        name = state.name ?: ""
        email = state.email ?: ""
    }

    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state.error != null) {
            toastMessage = state.error ?: context.getString(R.string.unknown_error)
            toastType = ToastType.ERROR
            showToast = true
            viewModel.clearError()
        } else if (!state.isLoading && state.success) {
            toastMessage = context.getString(R.string.profile_updated_successfully)
            toastType = ToastType.SUCCESS
            showToast = true
            viewModel.clearSuccess()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(visible = state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
        AnimatedVisibility(visible = !state.isLoading && state.error == null && state.name != null && state.email != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SehatinAppBar(
                    navigationIcon = true,
                    onNavigateUp = onNavigateUp
                )
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
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(.7f)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(text = stringResource(R.string.name)) },
                        isError = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = stringResource(R.string.email)) },
                        isError = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = { showConfirmDialog = true },
                    ) {
                        Text(
                            text = stringResource(R.string.save)
                        )
                    }
                }
            }
        }
    }

    if (showConfirmDialog) {
        CustomDialog(
            dialogTitle = stringResource(R.string.edit_profile_confirmation),
            dialogMessage = stringResource(R.string.are_you_sure_you_want_to_save_the_changes),
            onConfirm = {
                showConfirmDialog = false
                viewModel.updateProfile(name, email)
            },
            onDismiss = {
                showConfirmDialog = false
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


@Preview
@Composable
private fun ProfileScreenPreview() {
    SehatinTheme {
        ProfileScreen(
            onNavigateUp = {}
        )
    }
}