package com.c242_ps302.sehatin.presentation.screen.food

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _capturedImage = MutableLiveData<Bitmap?>(null)
    val capturedImage: LiveData<Bitmap?> = _capturedImage

    fun updateCapturedImage(bitmap: Bitmap) {
        Log.d("SharedViewModel", "Captured image updated: ${bitmap.width}x${bitmap.height}")
        _capturedImage.value = bitmap
        println("SharedViewModel: Captured image updated: ${bitmap.width}x${bitmap.height}")
    }
}
