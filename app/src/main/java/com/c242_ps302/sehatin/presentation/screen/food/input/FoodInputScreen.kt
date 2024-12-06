package com.c242_ps302.sehatin.presentation.screen.food.input

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme
import com.c242_ps302.sehatin.presentation.utils.bitmapToFile
import com.c242_ps302.sehatin.presentation.utils.compressImageSize
import com.c242_ps302.sehatin.presentation.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@Composable
fun FoodInputScreen(
    modifier: Modifier = Modifier,
    onNavigateToResult: () -> Unit,
    viewModel: FoodInputViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }
    val state by viewModel.foodInputState.collectAsStateWithLifecycle()

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                bitmapImage = null
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
            onResult = {
                bitmapImage = it
                imageUri = null
            })

    val camPermission =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                cameraLauncher.launch(null)
            } else {
                Toast.makeText(context, "Permission was not granted", Toast.LENGTH_SHORT).show()
            }
        }

    fun resetImage() {
        imageUri = null
        bitmapImage = null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn() + expandVertically()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }

        AnimatedVisibility(
            visible = state.error != null,
            enter = fadeIn() + expandVertically()
        ) {
            Text(
                text = state.error ?: "Unknown error occurred",
                color = MaterialTheme.colorScheme.error,
                maxLines = 2
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SehatinAppBar()
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Food Recognition",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (bitmapImage != null || imageUri != null) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUri)
                                .build(),
                            contentDescription = "Camera Image",
                            contentScale = ContentScale.Inside,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.8f)
                        )
                    }

                    if (bitmapImage != null) {
                        Image(
                            bitmap = bitmapImage!!.asImageBitmap(),
                            contentDescription = "Camera Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.8f)
                        )
                    }
                } else {
                    Text(
                        text = "Please Select an Image from Gallery or Open Camera",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (imageUri == null && bitmapImage == null) {
                        Button(onClick = {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }) {
                            Text(text = "Open Gallery")
                        }
                        Button(onClick = {
                            camPermission.launch(android.Manifest.permission.CAMERA)
                        }) {
                            Text(text = "Open Camera")
                        }
                    } else {
                        Button(onClick = {
                            try {
                                val foodImagePart = when {
                                    imageUri != null -> {
                                        val file = uriToFile(imageUri!!, context)
                                        val fileCompressed = file.compressImageSize()
                                        val requestFile = fileCompressed.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                        MultipartBody.Part.createFormData("file", file.name, requestFile)
                                    }
                                    bitmapImage != null -> {
                                        val file = bitmapToFile(bitmapImage!!, context)
                                        val fileCompressed = file.compressImageSize()
                                        val requestFile = fileCompressed.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                        MultipartBody.Part.createFormData("file", file.name, requestFile)
                                    }
                                    else -> null
                                }

                                foodImagePart?.let {
                                    Log.d("FoodInputScreen", "Sending prediction request")
                                    viewModel.postPrediction(it)
                                } ?: run {
                                    Toast.makeText(context, "No image to predict", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Log.e("FoodInputScreen", "Error in prediction", e)
                                Toast.makeText(context, "Error processing image: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        }) {
                            Text(text = "Predict")
                        }
                        Button(onClick = { resetImage() }) {
                            Text(text = "Retake Image")
                        }
                    }

                    AnimatedVisibility(
                        visible = !state.isLoading && state.success && state.error == null,
                        enter = fadeIn() + expandVertically()
                    ) {
                        Button(
                            onClick = { onNavigateToResult() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = "See Result")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FoodScreenPreview() {
    SehatinTheme {
        FoodInputScreen(
            onNavigateToResult = {}
        )
    }
}