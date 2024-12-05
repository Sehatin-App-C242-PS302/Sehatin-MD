package com.c242_ps302.sehatin.presentation.screen.food

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme

@Composable
fun FoodDetailScreen(
    navController: NavHostController? = null,
    cameraViewModel: CameraViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val capturedImage by cameraViewModel.capturedImage.collectAsState()

    if (capturedImage != null) {
        Image(painter = rememberImagePainter(capturedImage), contentDescription = "Captured Image")
    } else {
        Log.d("FoodDetailScreen", "Captured image is still null.")
    }

    LaunchedEffect(key1 = cameraViewModel.capturedImage) {
        cameraViewModel.capturedImage.collect { capturedImage ->
            if (capturedImage != null) {
                Log.d(
                    "FoodDetailScreen",
                    "Captured image updated: ${capturedImage.width}x${capturedImage.height}"
                )
            } else {
                Log.d("FoodDetailScreen", "Captured image is null, waiting...")
            }
        }
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
                Log.d(
                    "FoodDetailScreen",
                    "Captured image updated: ${capturedImage!!.width}x${capturedImage!!.height}"
                )
                Image(
                    bitmap = capturedImage!!.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier.size(200.dp)
                )
            } else {
                Log.d("FoodDetailScreen", "Captured image is null")
                Text("No image captured yet.")
            }

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
                    Text(text = "Fat", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                    Text(text = "Carbs", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                    Text(text = "Protein", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                    Text(text = "Calories", fontWeight = FontWeight.Bold, fontSize = 30.sp)
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

@Preview(showBackground = true)
@Composable
private fun FoodDetailScreenPreview() {
    SehatinTheme {
        FoodDetailScreen()
    }
}
