package com.c242_ps302.sehatin.presentation.screen.health_input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun HealthInputScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SehatinAppBar()
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Masukkan Data Kamu Yuk",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            DropdownMenu(
                expanded = TODO(),
                onDismissRequest = TODO()
            ) {
                
            }
        }
    }
}




@Composable
@Preview
fun HealthInputScreenPreview() {
    SehatinTheme {
        HealthInputScreen()
    }
}