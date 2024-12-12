package com.c242_ps302.sehatin.ui.screen.food.input

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.repository.PredictionRepository
import com.c242_ps302.sehatin.ui.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class FoodInputViewModel @Inject constructor(
    private val repository: PredictionRepository,
) : ViewModel() {
    private val _foodInputState = MutableStateFlow(FoodInputScreenUIState())
    val foodInputState = _foodInputState.asStateFlow()

    fun postPrediction(foodImage: MultipartBody.Part) = viewModelScope.launch {
        repository.postPrediction(foodImage).collectAndHandle(
            onError = { error ->
                _foodInputState.update {
                    it.copy(isLoading = false, error = error)
                }
                Log.d("FoodInputViewModel", "postPrediction: $error")
            },
            onLoading = {
                _foodInputState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) {
            _foodInputState.update {
                it.copy(isLoading = false, error = null, success = true)
            }
        }
    }

    fun clearError() {
        _foodInputState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _foodInputState.update {
            it.copy(success = false)
        }
    }
}

data class FoodInputScreenUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)