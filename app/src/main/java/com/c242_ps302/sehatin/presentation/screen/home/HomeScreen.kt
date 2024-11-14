package com.c242_ps302.sehatin.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        SehatinAppBar(
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(text = "Home Screen")
            Button(
                onClick = { onLogoutClick() }
            ) {
                Text(text = "Logout")
            }
        }
    }

}