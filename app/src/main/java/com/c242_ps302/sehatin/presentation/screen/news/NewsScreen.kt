package com.c242_ps302.sehatin.presentation.screen.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.c242_ps302.sehatin.domain.model.News
import com.c242_ps302.sehatin.presentation.components.NewsVerticalColumn
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    newsList: List<News>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        SehatinAppBar(
            modifier = modifier.fillMaxWidth()
        )
        NewsVerticalColumn(
            modifier = modifier.padding(top = 10.dp),
            newsList = newsList
        )
    }
}

