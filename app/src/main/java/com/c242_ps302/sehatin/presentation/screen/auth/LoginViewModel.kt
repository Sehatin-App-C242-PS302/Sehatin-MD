package com.c242_ps302.sehatin.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.repository.AuthRepository
import com.c242_ps302.sehatin.presentation.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginScreenUIState())
    val loginState = _loginState.asStateFlow()

    init {
        getToken()
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        repository.login(email, password).collectAndHandle(
            onError = { error ->
                _loginState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _loginState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) {
            _loginState.update {
                it.copy(isLoading = false, success = true, error = null)
            }
        }
    }

    private fun getToken() = viewModelScope.launch {
        repository.getToken().collect { token ->
            _loginState.update {
                it.copy(token = token)
            }
        }
    }

    fun clearError() {
        _loginState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _loginState.update {
            it.copy(success = false)
        }
    }
}

data class LoginScreenUIState(
    val token: String? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
)