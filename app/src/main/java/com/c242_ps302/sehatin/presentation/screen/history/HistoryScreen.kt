package com.c242_ps302.sehatin.presentation.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.c242_ps302.sehatin.presentation.components.card.HistoryCard
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    val historyList = viewModel.historyList.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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
                        text = "Health Data History",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
                items(historyList.value) { recommendation ->
                    HistoryCard(
                        recommendation = recommendation
                    )
                }
            }
        }
    }
}
