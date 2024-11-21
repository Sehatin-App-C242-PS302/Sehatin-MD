package com.c242_ps302.sehatin.presentation.screen.auth

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.display_text.SehatinDisplayText
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
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
                value = "",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.name)) }
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.email)) }
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { onRegisterClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.register)
                )
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
