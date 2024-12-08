package com.c242_ps302.sehatin.presentation.screen.food.result

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.presentation.components.card.FoodResultCard
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FoodResultScreen(
    modifier: Modifier = Modifier,
    onNavigateHome: () -> Unit,
    viewModel: FoodResultViewModel = hiltViewModel()
) {
    val state by viewModel.foodResultState.collectAsStateWithLifecycle()

    Scaffold {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                )
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            } else if (state.result != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    state.result?.let {
                        FoodResultCard(prediction = it)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = { onNavigateHome() }) {
                        Text(text = "Back to Home")
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun FoodResultScreenPreview() {
    SehatinTheme {
        FoodResultScreen(
            onNavigateHome = {}
        )
    }
}