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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.data.local.entity.RecommendationEntity
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.components.toast.SehatinToast
import com.c242_ps302.sehatin.presentation.components.toast.ToastType

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HealthResultScreen(
    onRecountClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: ResultViewModel = hiltViewModel()
) {
    val state by viewModel.healthResultState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state.error != null) {
            toastMessage = state.error ?: context.getString(R.string.unknown_error)
            toastType = ToastType.ERROR
            showToast = true
            viewModel.clearError()
        } else if (!state.isLoading && state.success) {
            toastMessage = context.getString(R.string.health_recommendation_loaded_successfully)
            toastType = ToastType.SUCCESS
            showToast = true
            viewModel.clearSuccess()
        }
    }

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                LoadingContent()
            }

            AnimatedVisibility(
                visible = state.error != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                ErrorContent(
                    error = state.error ?: "",
                    onBack = onBackClick
                )
            }

            AnimatedVisibility(
                visible = state.result != null && state.error == null && !state.isLoading,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                MainContent(
                    recommendation = state.result,
                    onRecountClick = onRecountClick,
                    onBackClick = onBackClick
                )
            }
        }

        if (showToast) {
            SehatinToast(
                message = toastMessage,
                type = toastType,
                duration = 2000L,
                onDismiss = { showToast = false }
            )
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
            text = stringResource(R.string.error, error),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onBack) {
            Text(text = stringResource(R.string.go_back))
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
                    text = stringResource(R.string.bmi_kamu, recommendation.category.toString()),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = stringResource(R.string.dengan_point),
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
                Text(text = stringResource(R.string.gender), style = MaterialTheme.typography.bodyLarge)
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
                        text = stringResource(R.string.age_health_result, rec.age ?: "-"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.height_health_result, rec.heightCm?.let {
                            String.format(
                                "%.1f cm",
                                it
                            )
                        } ?: "-"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.weight_health_result, rec.weightKg?.let {
                            String.format(
                                "%.1f kg",
                                it
                            )
                        } ?: "-"),
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
                        "underweight" -> stringResource(R.string.prioritaskan_asupan_nutrisi_yang_seimbang)
                        "overweight", "obese" -> stringResource(R.string.prioritaskan_olahraga_dan_diet_sehat)
                        else -> stringResource(R.string.pertahankan_pola_hidup_sehat)
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
                Text(text = stringResource(R.string.recount_bmi))
            }

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Text(text = stringResource(R.string.go_back_to_home))
            }
        }
    }
}




