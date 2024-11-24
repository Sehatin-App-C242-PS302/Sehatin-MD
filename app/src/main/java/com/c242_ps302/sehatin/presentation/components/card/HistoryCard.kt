package com.c242_ps302.sehatin.presentation.components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun HistoryCard(
    modifier: Modifier = Modifier,
    question: String,
    answer: String,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val transition = updateTransition(
        targetState = isExpanded,
        label = "Expand Card"
    )
    val iconRotationDegree by transition.animateFloat(label = "") { expandedState ->
        if (expandedState) 180f else 0f
    }
    val titleColor by transition.animateColor(label = "") { expandedState ->
        if (expandedState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    }
    val backgroundColorAlpha by transition.animateFloat(label = "") { expandedState ->
        if (expandedState) 1f else 0.5f
    }

    Card(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { isExpanded = !isExpanded }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = backgroundColorAlpha)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(9f),
                    text = question,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor
                )
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(iconRotationDegree)
                    ,
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand Card",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = backgroundColorAlpha)
                )
            }
            AnimatedVisibility(
                visible = isExpanded,
            ) {
                Text(
                    modifier = modifier.padding(top = 8.dp),
                    text = answer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}