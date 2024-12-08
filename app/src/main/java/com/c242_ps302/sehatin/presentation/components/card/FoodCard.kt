package com.c242_ps302.sehatin.presentation.components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.c242_ps302.sehatin.data.local.entity.PredictionEntity
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme
import com.c242_ps302.sehatin.presentation.utils.formatHistoryCardDate
import java.util.Locale

@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
    food: PredictionEntity,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val transition = updateTransition(
        targetState = isExpanded,
        label = "Expand Card"
    )
    val iconRotationDegree by transition.animateFloat(label = "") { expandedState ->
        if (expandedState) 180f else 0f
    }

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(food.imageUrl)
        .crossfade(true)
        .build()

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { isExpanded = !isExpanded }
            )
    ) {
        Column {
            if (!isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = food.predictedClass?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                            ?: "Unknown Food",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(6f)
                    )
                    Text(
                        text = food.createdAt.formatHistoryCardDate(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(4f)
                    )
                    Icon(
                        modifier = Modifier.weight(1f).rotate(iconRotationDegree),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand Card",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Expanded State: Show image and nutrition details
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AsyncImage(
                            model = imageRequest,
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .padding(16.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = food.predictedClass?.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                }
                                    ?: "Unknown Food",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = food.createdAt.formatHistoryCardDate(),
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                NutritionLabel("Calories")
                                NutritionLabel("Protein")
                                NutritionLabel("Fat")
                                NutritionLabel("Carbs")
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                NutritionValue(food.calories?.toString() ?: "0")
                                NutritionValue(food.protein?.toString() ?: "0")
                                NutritionValue(food.fat?.toString() ?: "0")
                                NutritionValue(food.carbohydrates?.toString() ?: "0")
                            }
                        }
                    }
                    Icon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .rotate(iconRotationDegree)
                            .padding(8.dp),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Collapse Card",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun NutritionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun NutritionValue(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
private fun FoodCardPreview() {
    SehatinTheme {
        FoodCard(
            food = PredictionEntity(
                predictedClass = "Nasi Goreng",
                calories = 250.0,
                protein = 8.5,
                fat = 12.3,
                carbohydrates = 30.0,
                createdAt = "Feb 27, 2023"
            )
        )
    }
}