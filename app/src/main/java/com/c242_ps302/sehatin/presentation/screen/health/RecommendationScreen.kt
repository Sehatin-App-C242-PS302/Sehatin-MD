package com.c242_ps302.sehatin.presentation.screen.health

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
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun RecommendationScreen(
    bmiResult: Double = 22.0, // Ganti dengan nilai BMI yang sebenarnya
    onRecountClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Section
        Text(
            text = "Sehatin",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BMI Result Section
        Text(
            text = "BMI Kamu Healthy",
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
            text = bmiResult.toString(),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Gender Icon and Info
        Text(text = "Gender", style = MaterialTheme.typography.bodyLarge)
        Icon(
            imageVector = Icons.Default.Male,
            contentDescription = "Male Gender",
            modifier = Modifier.size(48.dp)
        )
        Text(text = "Male", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // User Info Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Age : 20")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Height (cm)² : 176")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Weight (kg)² : 78")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BMI Indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFF6B6B), // Red
                            Color(0xFFFFD43B),  // Yellow
                            Color(0xFF51CF66), // Green
                            Color(0xFFFFD43B),  // Yellow
                            Color(0xFFFF6B6B) // Red
                        )
                    )
                )
        ) {
            // Indicator
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .offset(x = (bmiResult * 10).dp) // Adjust based on BMI value
                    .background(Color.Black)
                    .align(Alignment.CenterStart)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recommendation Text
        Text(
            text = "Prioritaskan Makan Sehat dan Olahraga",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Buttons
        Button(
            onClick = { onRecountClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Recount BMI")
        }

        OutlinedButton (
            onClick = { onBackClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Text(text = "Kembali Ke Beranda")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BMIResultScreenPreview() {
    SehatinTheme {
        RecommendationScreen(
            onRecountClick = {},
            onBackClick = {}
        )
    }
}
