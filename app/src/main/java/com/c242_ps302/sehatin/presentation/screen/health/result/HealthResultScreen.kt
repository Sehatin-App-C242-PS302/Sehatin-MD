package com.c242_ps302.sehatin.presentation.screen.health.result

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HealthResultScreen(
    onRecountClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel: ResultViewModel = hiltViewModel()
    val uiState by viewModel.healthResultState.collectAsState()

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                LoadingContent()
            }

            AnimatedVisibility(
                visible = uiState.error != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                ErrorContent(
                    error = uiState.error ?: "",
                    onBack = onBackClick
                )
            }

            AnimatedVisibility(
                visible = uiState.result != null && uiState.error == null && !uiState.isLoading,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                MainContent(
                    recommendation = uiState.result,
                    onRecountClick = onRecountClick,
                    onBackClick = onBackClick
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $error",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onBack) {
            Text(text = "Kembali")
        }
    }
}


@Composable
private fun MainContent(
    recommendation: RecommendationEntity?,
    onRecountClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SehatinAppBar()

        Spacer(modifier = Modifier.height(24.dp))

        recommendation?.let { rec ->
            // BMI Result Section
            rec.bmi?.let { bmi ->
                Text(
                    text = "BMI Kamu ${getBMICategory(bmi)}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Dengan Point",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = String.format("%.1f", bmi),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gender section
            rec.gender?.let { gender ->
                Text(text = "Gender", style = MaterialTheme.typography.bodyLarge)
                Icon(
                    imageVector = if (gender.lowercase() == "male")
                        Icons.Default.Male else Icons.Default.Female,
                    contentDescription = "$gender Gender",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = gender,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Age: ${rec.age ?: "-"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Height: ${
                            rec.heightCm?.let {
                                String.format(
                                    "%.1f cm",
                                    it
                                )
                            } ?: "-"
                        }",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Weight: ${
                            rec.weightKg?.let {
                                String.format(
                                    "%.1f kg",
                                    it
                                )
                            } ?: "-"
                        }",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BMI Indicator
            rec.bmi?.let { bmi ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFFF6B6B), // Underweight
                                    Color(0xFFFFD43B), // Normal low
                                    Color(0xFF51CF66), // Normal
                                    Color(0xFFFFD43B), // Overweight
                                    Color(0xFFFF6B6B)  // Obese
                                )
                            )
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp) // Sedikit lebih besar untuk lebih terlihat
                            .offset(
                                x = (bmi * 10).coerceIn(
                                    0.0,
                                    400.0
                                ).dp
                            ) // Membatasi pergerakan indicator
                            .background(
                                Color.Black,
                                RoundedCornerShape(4.dp)
                            ) // Tambahkan rounded corner
                            .align(Alignment.CenterStart)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            rec.category?.let { category ->
                Text(
                    text = when (category.lowercase()) {
                        "underweight" -> "Prioritaskan Asupan Nutrisi yang Seimbang"
                        "overweight", "obese" -> "Prioritaskan Olahraga dan Diet Sehat"
                        else -> "Pertahankan Pola Hidup Sehat"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Buttons
            Button(
                onClick = onRecountClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Hitung Ulang BMI")
            }

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Text(text = "Kembali Ke Beranda")
            }
        }
    }
}

private fun getBMICategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25.0 -> "Normal"
        bmi < 30.0 -> "Overweight"
        else -> "Obese"
    }
}



