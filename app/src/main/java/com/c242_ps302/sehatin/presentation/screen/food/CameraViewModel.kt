package com.c242_ps302.sehatin.presentation.screen.food

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel : ViewModel() {
    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
    val capturedImage: StateFlow<Bitmap?> = _capturedImage

    fun onTakePhoto(bitmap: Bitmap) {
        Log.d("CameraViewModel", "New image captured: ${bitmap.width}x${bitmap.height}")
        Log.d("CameraViewModel", "Updating captured image: ${bitmap.width}x${bitmap.height}")
        _capturedImage.value = bitmap
    }
}