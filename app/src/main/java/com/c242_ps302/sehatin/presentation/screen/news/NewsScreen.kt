package com.c242_ps302.sehatin.presentation.screen.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.NewsVerticalColumn
import com.c242_ps302.sehatin.presentation.utils.searchKeywords

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel(),
) {
    val newsList by viewModel.newsList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSuggestionChipVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when {
            uiState.error != null -> {
                // Handle error
            }

            uiState.success -> {
                viewModel.clearError()
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = {
                viewModel.onSearchQueryChanged(it)
            },
            onSearch = {
                viewModel.onSearchQueryChanged(searchQuery)
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            active = false,
            onActiveChange = { },
            placeholder = { Text(stringResource(R.string.searchbar_text)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (searchQuery.isNotEmpty()) {
                            viewModel.onSearchQueryChanged("")
                        } else {
                            viewModel.refreshNews()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { isSuggestionChipVisible = it.isFocused },
            content = {}
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(
            visible = isSuggestionChipVisible
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(searchKeywords) { keyword ->
                    SuggestionChip(
                        onClick = {
                            viewModel.onSearchQueryChanged(keyword)
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        },
                        label = { Text(text = keyword) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.size(50.dp)
                )

                uiState.error != null -> Text(
                    text = uiState.error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                newsList.isEmpty() -> Text(
                    text = "Tidak ada berita ditemukan",
                    modifier = Modifier.padding(16.dp)
                )

                else -> NewsVerticalColumn(
                    modifier = Modifier.fillMaxSize(),
                    newsList = newsList
                )
            }
        }
    }
}