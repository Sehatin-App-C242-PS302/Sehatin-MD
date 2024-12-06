package com.c242_ps302.sehatin.presentation.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data("https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg")
        .crossfade(true)
        .build()

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Gambar di kiri
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp) // Ukuran gambar
                    .padding(16.dp) // Padding di sekitar gambar
            )

            // Teks di kanan
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Camera",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Feb 27, 2023",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Left
                )

                Spacer(modifier = Modifier.height(8.dp)) // Spasi antara bagian atas dan bawah

                // Nutrisi
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Fat",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Carbs",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Protein",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "0.8g",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Left
                    )
                    Text(
                        text = "2.4g",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Left
                    )
                    Text(
                        text = "1.6g",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Left
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FoodCardPreview() {
    SehatinTheme {
        FoodCard()
    }
}