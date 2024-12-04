package com.c242_ps302.sehatin.presentation.screen.food

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun FoodDetailScreen(
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    val capturedImage = sharedViewModel.capturedImage.value

    LaunchedEffect(capturedImage) {
        Log.d("FoodDetailScreen", "Captured image updated: ${capturedImage?.width}x${capturedImage?.height}")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SehatinAppBar()

            Spacer(modifier = Modifier.height(40.dp))

            if (capturedImage != null) {
                Image(
                    bitmap = capturedImage.asImageBitmap(),
                    contentDescription = "Captured Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .size(200.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                    contentDescription = "Default Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .size(200.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Broccoli",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
                Text(
                    text = "Feb 27, 2023",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Fat",
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                        Text(
                            text = "Carbs",
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                        Text(
                            text = "Protein",
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                        Text(
                            text = "Calories",
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "0.8g", fontSize = 28.sp)
                        Text(text = "2.4g", fontSize = 28.sp)
                        Text(text = "1.6g", fontSize = 28.sp)
                        Text(text = "69g", fontSize = 28.sp)
                    }
                }
            }
        }
    }
}
@Preview
@Composable
private fun DetailFoodScreenPreview() {
    SehatinTheme {
        FoodDetailScreen(sharedViewModel = SharedViewModel())
    }
}