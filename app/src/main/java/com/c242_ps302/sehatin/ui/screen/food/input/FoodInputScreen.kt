package com.c242_ps302.sehatin.ui.screen.food.input

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
import com.c242_ps302.sehatin.ui.components.sehatin_appbar.SehatinAppBar
import com.c242_ps302.sehatin.ui.components.toast.SehatinToast
import com.c242_ps302.sehatin.ui.components.toast.ToastType
import com.c242_ps302.sehatin.ui.theme.SehatinTheme
import com.c242_ps302.sehatin.ui.utils.bitmapToFile
import com.c242_ps302.sehatin.ui.utils.compressImageSize
import com.c242_ps302.sehatin.ui.utils.uriToFile
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
                Toast.makeText(context,
                    context.getString(R.string.permission_was_not_granted), Toast.LENGTH_SHORT).show()
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
            toastMessage = state.error ?: context.getString(R.string.unknown_error)
            toastType = ToastType.ERROR
            showToast = true
            viewModel.clearError()
        } else if (!state.isLoading && state.success) {
            toastMessage = context.getString(R.string.prediction_uploaded_successfully)
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
                    text = stringResource(R.string.food_recognition),
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
                                contentDescription = stringResource(R.string.camera_image),
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f)
                            )
                        } else if (bitmapImage != null) {
                            Image(
                                bitmap = bitmapImage!!.asImageBitmap(),
                                contentDescription = stringResource(R.string.camera_image),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f)
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.please_select_an_image_from_gallery_or_open_camera),
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
                            Text(text = stringResource(R.string.open_gallery))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                camPermission.launch(android.Manifest.permission.CAMERA)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(R.string.open_camera))
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
                                            context.getString(R.string.no_image_to_predict),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("FoodInputScreen", "Error in prediction", e)
                                    Toast.makeText(
                                        context,
                                        context.getString(
                                            R.string.error_processing_image,
                                            e.message
                                        ),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(R.string.predict))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedButton(
                            onClick = { resetImage() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(R.string.reselect_image))
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

    if (showToast) {
        SehatinToast(
            message = toastMessage,
            type = toastType,
            duration = 1000L,
            onDismiss = { showToast = false }
        )
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