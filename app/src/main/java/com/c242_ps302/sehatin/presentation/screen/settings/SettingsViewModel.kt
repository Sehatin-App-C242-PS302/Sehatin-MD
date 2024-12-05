package com.c242_ps302.sehatin.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.c242_ps302.sehatin.data.local.entity.UserEntity
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.repository.AuthRepository
import com.c242_ps302.sehatin.data.repository.Result
import com.c242_ps302.sehatin.presentation.notification.DailyReminderWorker
import com.c242_ps302.sehatin.presentation.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: SehatinAppPreferences,
    private val repository: AuthRepository,
    private val workManager: WorkManager,
) : ViewModel() {
    private val _settingsState = MutableStateFlow(SettingsScreenUIState())
    val settingsState = _settingsState.asStateFlow()

    init {
        getUserData()
    }

    private fun getUserData() = viewModelScope.launch {
        repository.getUser().collectAndHandle(
            onError = { error ->
                _settingsState.update {
                    it.copy(isLoading = false, error = error)
                }
            },
            onLoading = {
                _settingsState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { user ->
            _settingsState.update {
                it.copy(isLoading = false, user = user, error = null)
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            when (val result = repository.logout()) {
                is Result.Success -> {
                    // Logout successful
                    onLogoutSuccess()
                }

                is Result.Error -> {
                    // Log the error, but still proceed with logout
                    _settingsState.update {
                        it.copy(
                            error = result.error ?: "Logout failed"
                        )
                    }
                    onLogoutSuccess()
                }

                is Result.Loading -> {
                    _settingsState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun toggleDarkTheme() {
        viewModelScope.launch {
            val newValue = !_settingsState.value.isDarkTheme
            preferences.setDarkTheme(newValue)
            _settingsState.update { currentState ->
                currentState.copy(isDarkTheme = newValue)
            }
        }
    }

    fun toggleNotification(isNotificationEnabled: Boolean) {
        viewModelScope.launch {
            preferences.setNotificationEnable(isNotificationEnabled)
            _settingsState.update { currentState ->
                currentState.copy(isNotificationEnabled = isNotificationEnabled)
            }

            if (isNotificationEnabled) {
                DailyReminderWorker.scheduleDaily(workManager)
                DailyReminderWorker.scheduleImmediateReminder(workManager)
            } else {
                workManager.cancelUniqueWork("daily_reminder")
            }
        }
    }
}


data class SettingsScreenUIState(
    val user: UserEntity? = null,
    val isDarkTheme: Boolean = false,
    val isNotificationEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)