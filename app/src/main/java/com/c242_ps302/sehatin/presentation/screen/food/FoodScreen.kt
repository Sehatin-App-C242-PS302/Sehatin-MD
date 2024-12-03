package com.c242_ps302.sehatin.presentation.screen.food

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.navigation.Routes

@Composable
fun FoodScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Create the launcher for picking an image from the gallery
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        // Handle the selected image URI (you can update state, navigate, etc.)
        uri?.let {
            // Do something with the selected image URI (e.g., display the image)
            // For now, it just logs the selected URI.
            Log.d("FoodScreen", "Selected image URI: $uri")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SehatinAppBar()
            Spacer(modifier = Modifier.height(80.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Take a picture of food or select from gallery",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .weight(1f)
                            .height(150.dp)
                            .clickable {
                                // Launch the photo picker
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Image(
                                modifier = Modifier.aspectRatio(1f),
                                painter = painterResource(id = R.drawable.baseline_add_photo_alternate_24),
                                contentDescription = "add photo"
                            )
                        }
                    }
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .weight(1f)
                            .height(150.dp)
                            .clickable { navController.navigate(Routes.CameraScreen) }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Image(
                                modifier = Modifier.size(230.dp),
                                painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                                contentDescription = "take photo"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        text = "Recent Food",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Left
                    )
                    // Example for the recent food list - You can repeat this for multiple items
                    Spacer(modifier = Modifier.height(20.dp))
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .size(342.dp, 134.dp)
                            .padding(20.dp)
                            .fillMaxWidth(0.95f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Image(
                                modifier = Modifier.size(114.dp),
                                painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                                contentDescription = "take photo"
                            )
                            Column {
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
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
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
                                    horizontalArrangement = Arrangement.SpaceEvenly,
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
            }
        }
    }
}