package com.c242_ps302.sehatin.presentation.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = "Register Screen",
                color = MaterialTheme.colorScheme.onBackground  // Eksplisit gunakan warna teks yang sesuai
            )
            Button(
                onClick = { onRegisterClick() }
            ) {
                Text(text = "Register")
            }
        }
    }
}
