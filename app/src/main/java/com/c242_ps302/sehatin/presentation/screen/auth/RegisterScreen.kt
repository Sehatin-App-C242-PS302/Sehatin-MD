package com.c242_ps302.sehatin.presentation.screen.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.display_text.SehatinDisplayText
import com.c242_ps302.sehatin.presentation.components.toast.SehatinToast
import com.c242_ps302.sehatin.presentation.components.toast.ToastType
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.registerState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<Int?>(null) }
    var emailError by remember { mutableStateOf<Int?>(null) }
    var passwordError by remember { mutableStateOf<Int?>(null) }

    var showPassword by remember { mutableStateOf(false) }

    fun validateEmptyFields(): Boolean {
        return if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            nameError = if (name.isEmpty()) R.string.name_field_is_required else null
            emailError = if (email.isEmpty()) R.string.email_field_is_required else null
            passwordError = if (password.isEmpty()) R.string.password_field_is_required else null
            false
        } else {
            nameError = null
            emailError = null
            passwordError = null
            true
        }
    }

    fun validateEmail(email: String): Boolean {
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = R.string.email_format_is_invalid
            false
        } else {
            emailError = null
            true
        }
    }

    fun validatePassword(password: String): Boolean {
        return if (password.length < 6) {
            passwordError = R.string.password_must_be_at_least_6_characters
            false
        } else {
            passwordError = null
            true
        }
    }

    fun validateForm(): Boolean {
        val isInputNotEmpty = validateEmptyFields()
        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)
        return isInputNotEmpty && isEmailValid && isPasswordValid
    }

    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state.error != null) {
            toastMessage = state.error ?: context.getString(R.string.unknown_error)
            toastType = ToastType.ERROR
            showToast = true
            viewModel.clearError()
        } else if (!state.isLoading && state.success) {
            toastMessage = context.getString(R.string.register_success)
            toastType = ToastType.SUCCESS
            showToast = true
            viewModel.clearSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
                .padding(horizontal = 50.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome_to),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            SehatinDisplayText(title = stringResource(R.string.app_name))
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                modifier = Modifier.size(230.dp),
                painter = painterResource(id = R.drawable.img_onboarding),
                contentDescription = stringResource(R.string.onboarding_image)
            )
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = { Text(text = stringResource(R.string.name)) },
                isError = nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            nameError?.let {
                Text(
                    text = stringResource(id = it),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = { Text(text = stringResource(R.string.email)) },
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            emailError?.let {
                Text(
                    text = stringResource(id = it),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text(text = stringResource(R.string.password)) },
                visualTransformation = if (showPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                isError = passwordError != null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword)
                                stringResource(R.string.hide_password)
                            else
                                stringResource(R.string.show_password),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            passwordError?.let {
                Text(
                    text = stringResource(id = it),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (validateForm()) {
                        viewModel.register(name, email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = stringResource(R.string.register))
                }
            }

            Spacer(modifier = Modifier.height(3.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account)
                )
                TextButton(
                    onClick = { onLoginClick() }
                ) {
                    Text(
                        text = stringResource(R.string.login)
                    )
                }
            }
        }
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            onRegisterClick()
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

@Preview
@Composable
fun RegisterScreenPreview() {
    SehatinTheme {
        RegisterScreen(
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
