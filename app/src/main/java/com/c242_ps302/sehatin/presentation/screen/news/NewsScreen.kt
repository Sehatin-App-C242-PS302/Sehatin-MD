package com.c242_ps302.sehatin.presentation.screen.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.c242_ps302.sehatin.presentation.components.card.NewsCard
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        SehatinAppBar(
            modifier = modifier.fillMaxWidth()
        )
        LazyColumn {
            items(20) {
                NewsCard()
            }
        }
    }
}

