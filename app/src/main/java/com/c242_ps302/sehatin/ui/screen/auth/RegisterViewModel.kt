package com.c242_ps302.sehatin.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.repository.AuthRepository
import com.c242_ps302.sehatin.ui.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _registerState = MutableStateFlow(RegisterScreenUIState())
    val registerState = _registerState.asStateFlow()

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        repository.register(name, email, password).collectAndHandle(
            onError = { error ->
                _registerState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _registerState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) {
            _registerState.update {
                it.copy(isLoading = false, success = true, error = null)
            }
        }
    }

    fun clearError() {
        _registerState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _registerState.update {
            it.copy(success = false)
        }
    }
}

data class RegisterScreenUIState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
)