package com.c242_ps302.sehatin.presentation.screen.food.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.presentation.components.card.FoodResultCard
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun FoodResultScreen(
    modifier: Modifier = Modifier,
    onNavigateHome: () -> Unit,
    viewModel: FoodResultViewModel = hiltViewModel()
) {
    val state by viewModel.foodResultState.collectAsStateWithLifecycle()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedVisibility(visible = state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
        AnimatedVisibility(visible = state.error != null) {
            Text(
                text = state.error ?: "unknown error",
                color = MaterialTheme.colorScheme.error,
                maxLines = 2
            )
        }
        AnimatedVisibility(visible = !state.isLoading && state.error == null && state.result != null) {
            state.result?.let { FoodResultCard(prediction = it) }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { onNavigateHome()}
            ) {
                Text(text = "Back to Home")
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