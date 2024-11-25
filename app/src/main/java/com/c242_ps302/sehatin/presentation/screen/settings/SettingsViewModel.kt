package com.c242_ps302.sehatin.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c242_ps302.sehatin.data.local.entity.UserEntity
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.repository.AuthRepository
import com.c242_ps302.sehatin.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: SehatinAppPreferences,
    private val authRepository: AuthRepository,

    ) : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    private val _isNotificationEnabled = MutableStateFlow(false)
    val isNotificationEnabled = _isNotificationEnabled.asStateFlow()

    private val _userState = MutableStateFlow<Result<UserEntity>>(Result.Loading)
    val userState = _userState.asStateFlow()


    init {
        viewModelScope.launch {
            _isDarkTheme.value = preferences.getDarkTheme()
            _isNotificationEnabled.value = preferences.getNotificationEnable()
            getUserData()
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            authRepository.getUser().collect { result ->
                _userState.value = result
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            when (authRepository.logout()) {
                is Result.Success -> onLogoutSuccess()
                is Result.Error -> {} // Handle error if needed
                Result.Loading -> {} // Handle loading if needed
            }
        }
    }

    fun toggleDarkTheme() {
        viewModelScope.launch {
            val newValue = !_isDarkTheme.value
            preferences.setDarkTheme(newValue)
            _isDarkTheme.value = newValue
        }
    }

    fun toggleNotification(isNotificationEnabled: Boolean) {
        viewModelScope.launch {
            preferences.setNotificationEnable(isNotificationEnabled)
            _isNotificationEnabled.value = isNotificationEnabled
        }
    }
}