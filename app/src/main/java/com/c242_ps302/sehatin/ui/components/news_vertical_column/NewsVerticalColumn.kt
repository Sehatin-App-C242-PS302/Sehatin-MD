package com.c242_ps302.sehatin.ui.components.news_vertical_column

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.c242_ps302.sehatin.domain.model.News
import com.c242_ps302.sehatin.ui.components.card.NewsCard

@Composable
fun NewsVerticalColumn(
    modifier: Modifier = Modifier,
    newsList: List<News>,
    onNewsClick: (String) -> Unit,
) {

    LazyColumn(
        contentPadding = PaddingValues(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(count = newsList.size, key = {
            newsList[it].url
        }) { index ->
            val news = newsList[index]
            NewsCard(
                news = news,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable { onNewsClick(news.url) }
            )
        }
    }
}