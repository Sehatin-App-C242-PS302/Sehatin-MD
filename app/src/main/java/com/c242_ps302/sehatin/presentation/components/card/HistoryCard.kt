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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme
import com.c242_ps302.sehatin.presentation.utils.formatHistoryCardDate

@Composable
fun HistoryCard(
    modifier: Modifier = Modifier,
    recommendation: RecommendationEntity
) {
    val context = LocalContext.current

    var isExpanded by remember { mutableStateOf(false) }
    val transition = updateTransition(
        targetState = isExpanded,
        label = stringResource(R.string.expand_card)
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category on the left
                Text(
                    modifier = Modifier.weight(6f),
                    text = recommendation.category ?: stringResource(R.string.no_category),
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor
                )

                // Date on the right
                Text(
                    modifier = Modifier.weight(4f),
                    text = recommendation.createdAt.formatHistoryCardDate(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Expand Icon
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(iconRotationDegree),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.expand_card),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = backgroundColorAlpha)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.gender_hitory_card,
                            recommendation.gender ?: context.getString(R.string.unknown)
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(
                            R.string.age_history_card,
                            recommendation.age ?: context.getString(R.string.unknown)
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(
                            R.string.weight_kg_history_card,
                            recommendation.weightKg ?: context.getString(R.string.unknown)
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(
                            R.string.height_cm_history_card,
                            recommendation.heightCm ?: context.getString(R.string.unknown)
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(
                            R.string.bmi_history_card,
                            recommendation.bmi ?: context.getString(R.string.unknown)
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(
                            R.string.daily_step_recommendation_history_card,
                            recommendation.dailyStepRecommendation ?: context.getString(R.string.unknown)
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryCardPreview() {
    SehatinTheme {
        HistoryCard(
            recommendation = RecommendationEntity(
                gender = "Male",
                weightKg = 75.0,
                heightCm = 175.0,
                category = "Healthy",
                age = 25,
                bmi = 24.5,
                dailyStepRecommendation = "10,000 steps",
                createdAt = "Monday, 12 Desember 2024"
            )
        )
    }
}
