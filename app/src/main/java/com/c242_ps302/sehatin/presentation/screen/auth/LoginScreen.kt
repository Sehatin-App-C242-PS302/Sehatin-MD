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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.display_text.SehatinDisplayText
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<Int?>(null) }
    var passwordError by remember { mutableStateOf<Int?>(null) }

    fun validateEmptyFields(): Boolean {
        return if (email.isEmpty() || password.isEmpty()) {
            emailError = if (email.isEmpty()) R.string.email_field_is_required else null
            passwordError = if (password.isEmpty()) R.string.password_field_is_required else null
            false
        } else {
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
            Spacer(modifier = Modifier.height(50.dp))

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
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

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text(text = stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
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

            // Error message from API
            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (validateForm()) {
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = stringResource(R.string.login))
                }
            }

            Spacer(modifier = Modifier.height(3.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.don_t_have_an_account))
                TextButton(onClick = onRegisterClick) {
                    Text(text = stringResource(R.string.register_now))
                }
            }
        }
    }

    // Handle success state
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onLoginClick()
        }
    }

    // Handle existing token
    LaunchedEffect(viewModel.token.collectAsStateWithLifecycle().value) {
        if (!viewModel.token.value.isNullOrEmpty()) {
            onLoginClick()
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    SehatinTheme {
        LoginScreen(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

