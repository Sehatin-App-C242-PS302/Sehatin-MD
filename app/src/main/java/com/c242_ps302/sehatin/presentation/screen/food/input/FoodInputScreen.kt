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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.presentation.components.toast.ToastType
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

    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state.error != null) {
            toastMessage = state.error ?: "Unknown error"
            toastType = ToastType.ERROR
            showToast = true
        } else if (!state.isLoading && state.success) {
            toastMessage = "Prediction uploaded successfully!"
            toastType = ToastType.SUCCESS
            showToast = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn() + expandVertically(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        AnimatedVisibility(
            visible = !state.isLoading,
            enter = fadeIn() + expandVertically(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SehatinAppBar()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Food Recognition",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
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
                                    .fillMaxHeight(0.6f)
                            )
                        } else if (bitmapImage != null) {
                            Image(
                                bitmap = bitmapImage!!.asImageBitmap(),
                                contentDescription = "Camera Image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f)
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
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (imageUri == null && bitmapImage == null) {
                        Button(
                            onClick = {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Open Gallery")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                camPermission.launch(android.Manifest.permission.CAMERA)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Open Camera")
                        }
                    } else {
                        Button(
                            onClick = {
                                try {
                                    val foodImagePart = when {
                                        imageUri != null -> {
                                            val file = uriToFile(imageUri!!, context)
                                            val fileCompressed = file.compressImageSize()
                                            val requestFile =
                                                fileCompressed.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                            MultipartBody.Part.createFormData(
                                                "file",
                                                file.name,
                                                requestFile
                                            )
                                        }

                                        bitmapImage != null -> {
                                            val file = bitmapToFile(bitmapImage!!, context)
                                            val fileCompressed = file.compressImageSize()
                                            val requestFile =
                                                fileCompressed.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                            MultipartBody.Part.createFormData(
                                                "file",
                                                file.name,
                                                requestFile
                                            )
                                        }

                                        else -> null
                                    }

                                    foodImagePart?.let {
                                        Log.d("FoodInputScreen", "Sending prediction request")
                                        viewModel.postPrediction(it)
                                    } ?: run {
                                        Toast.makeText(
                                            context,
                                            "No image to predict",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("FoodInputScreen", "Error in prediction", e)
                                    Toast.makeText(
                                        context,
                                        "Error processing image: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Predict")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedButton(
                            onClick = { resetImage() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Retake Image")
                        }
                    }
                }


                AnimatedVisibility(
                    visible = !state.isLoading && state.success && state.error == null,
                    enter = fadeIn() + expandVertically(),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Button(
                        onClick = { onNavigateToResult() },
                        modifier = Modifier.fillMaxWidth(.9f)
                    ) {
                        Text(text = stringResource(R.string.see_result))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                        )
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