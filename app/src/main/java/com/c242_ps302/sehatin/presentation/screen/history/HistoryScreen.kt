package com.c242_ps302.sehatin.presentation.screen.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.card.FoodCard
import com.c242_ps302.sehatin.presentation.components.card.HistoryCard
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.components.toast.SehatinToast
import com.c242_ps302.sehatin.presentation.components.toast.ToastType

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    historyViewModel: HistoryViewModel = hiltViewModel(),
) {
    val state by historyViewModel.historyState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state.foodError != null || state.historyError != null) {
            toastMessage =
                state.foodError ?: state.historyError ?: context.getString(R.string.unknown_error)
            toastType = ToastType.ERROR
            showToast = true
            historyViewModel.clearError()
        } else if (state.foodSuccess || state.historySuccess && !state.historyIsLoading && !state.foodIsLoading) {
            toastMessage = context.getString(R.string.history_loaded_successfully)
            toastType = ToastType.SUCCESS
            showToast = true
            historyViewModel.clearSuccess()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = state.foodIsLoading || state.historyIsLoading,
            enter = fadeIn() + expandVertically()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
        AnimatedVisibility(visible = state.foodError != null || state.historyError != null) {
            Text(
                text = state.foodError ?: state.historyError
                ?: context.getString(R.string.unknown_error),
                color = MaterialTheme.colorScheme.error,
                maxLines = 2
            )
        }
        AnimatedVisibility(visible = !state.foodIsLoading && !state.historyIsLoading && state.history.isNotEmpty() && state.foods.isNotEmpty() && state.historySuccess && state.foodSuccess) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SehatinAppBar()
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.health_data_history),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    items(state.history) { recommendation ->
                        HistoryCard(recommendation = recommendation)
                    }
                    item {
                        Text(
                            text = stringResource(R.string.food_recognition_history),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    items(state.foods) { food ->
                        FoodCard(food = food)
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
}