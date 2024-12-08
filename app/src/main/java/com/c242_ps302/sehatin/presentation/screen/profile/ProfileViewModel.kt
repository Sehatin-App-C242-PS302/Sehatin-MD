package com.c242_ps302.sehatin.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.repository.UserRepository
import com.c242_ps302.sehatin.presentation.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileScreenUiState())
    val profileState = _profileState.asStateFlow()

    init {
        getUserData()
    }

    fun updateProfile(name: String, email: String) = viewModelScope.launch {
        repository.updateProfile(name, email).collectAndHandle(onError = { error ->
            _profileState.update {
                it.copy(isLoading = false, error = error)
            }
        }, onLoading = {
            _profileState.update {
                it.copy(isLoading = true, error = null)
            }
        }) { user ->
            _profileState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    success = true,
                    name = user.name,
                    email = user.email
                )
            }
        }
    }

    private fun getUserData() = viewModelScope.launch {
        repository.getUserData().collectAndHandle(
            onError = { error ->
                _profileState.update {
                    it.copy(isLoading = false, error = error)
                }
            }, onLoading = {
                _profileState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { user ->
            _profileState.update {
                it.copy(isLoading = false, error = null, name = user.name, email = user.email)
            }
        }
    }

    fun clearError() {
        _profileState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _profileState.update {
            it.copy(success = false)
        }
    }
}

data class ProfileScreenUiState(
    val name: String? = null,
    val email: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)