@file:OptIn(ExperimentalMaterial3Api::class)

package com.c242_ps302.sehatin.presentation.screen.food

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.camera.core.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.c242_ps302.sehatin.presentation.navigation.Routes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreenWrapper(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    CameraContent(navController, cameraPermissionState.status.isGranted, sharedViewModel)

    if (cameraPermissionState.status.shouldShowRationale) {
        // Show rationale for camera permission
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            title = { Text("Camera Permission Required") },
            text = { Text("This app needs access to your camera to take photos.") },
            confirmButton = {
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            },
            dismissButton = {
                Button(onClick = { /* Handle dismissal, e.g., navigate back */ }) {
                    Text("Deny")
                }
            }
        )
    }
}

@Composable
fun CameraContent(navController: NavHostController, isGranted: Boolean, sharedViewModel: SharedViewModel) {
    if (isGranted) {
        CameraScreen(navController, sharedViewModel)
    } else {
        CameraPermissionRequest()
    }
}

@Composable
fun CameraPermissionRequest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Camera permission is required to use this feature.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Handle permission request */ }) {
            Text("Grant Permission")
        }
    }
}

@Composable
fun CameraScreen(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val previewView = remember { PreviewView(context) }
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(lensFacing) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also { previewUseCase ->
                previewUseCase.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.Builder().requireLensFacing(lensFacing).build(),
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.e("Camera", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = {
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                    CameraSelector.LENS_FACING_FRONT
                } else {
                    CameraSelector.LENS_FACING_BACK
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Switch Camera"
            )
        }

        IconButton(
            onClick = {
                takePhoto(context, imageCapture) { bitmap ->
                    sharedViewModel.updateCapturedImage(bitmap)
                    println("Captured image before navigating: ${bitmap.width}x${bitmap.height}")
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(Routes.FoodDetailScreen)
                    }, 300)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "Take Photo"
            )
        }
    }
}


private fun takePhoto(context: Context, imageCapture: ImageCapture?, onPhotoTaken: (Bitmap) -> Unit) {
    imageCapture?.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val bitmap = image.toBitmap()?.let { originalBitmap ->
                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    Bitmap.createBitmap(
                        originalBitmap,
                        0,
                        0,
                        originalBitmap.width,
                        originalBitmap.height,
                        matrix,
                        true
                    )
                }
                if (bitmap != null) {
                    Log.d("CameraScreen", "Bitmap captured successfully: ${bitmap.width}x${bitmap.height}")
                    onPhotoTaken(bitmap)
                } else {
                    Log.e("CameraScreen", "Bitmap conversion failed!")
                }
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Photo capture failed", exception)
            }
        }
    )
}

private fun ImageProxy.toBitmap(): Bitmap? {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}



