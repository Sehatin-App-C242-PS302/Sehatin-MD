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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.card.FoodResultCard
import com.c242_ps302.sehatin.presentation.components.toast.SehatinToast
import com.c242_ps302.sehatin.presentation.components.toast.ToastType
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FoodResultScreen(
    modifier: Modifier = Modifier,
    onNavigateHome: () -> Unit,
    viewModel: FoodResultViewModel = hiltViewModel()
) {
    val state by viewModel.foodResultState.collectAsStateWithLifecycle()

    val context = LocalContext.current

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
            toastMessage = context.getString(R.string.prediction_loaded_successfully)
            toastType = ToastType.SUCCESS
            showToast = true
        }
    }

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
                    text = state.error ?: stringResource(id = R.string.unknown_error),
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
                        Text(text = stringResource(R.string.back_to_home))
                    }
                }
            }
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
private fun FoodResultScreenPreview() {
    SehatinTheme {
        FoodResultScreen(
            onNavigateHome = {}
        )
    }
}