package com.c242_ps302.sehatin.ui.components.display_text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun SehatinDisplayText(
    title: String
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(
                    title.substring(0, 5)
                )
            }
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.inversePrimary)) {
                append(
                    title.substring(5)
                )
            }
        },
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.ExtraBold
    )
}