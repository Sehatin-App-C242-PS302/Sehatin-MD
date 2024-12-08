package com.c242_ps302.sehatin.presentation.screen.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.c242_ps302.sehatin.data.local.entity.UserEntity
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.data.repository.AuthRepository
import com.c242_ps302.sehatin.data.repository.Result
import com.c242_ps302.sehatin.presentation.notification.DailyReminderNotificationHelper.Companion.DAILY_REMINDER_WORK_NAME
import com.c242_ps302.sehatin.presentation.notification.DailyReminderWorker
import com.c242_ps302.sehatin.presentation.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: SehatinAppPreferences,
    private val repository: AuthRepository,
    private val workManager: WorkManager,
) : ViewModel() {
    private val _settingsState = MutableStateFlow(SettingsScreenUIState())
    val settingsState = _settingsState.asStateFlow()

    val notificationsEnabled = preferences.getNotificationFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    init {
        viewModelScope.launch {
            val isDarkTheme = preferences.getDarkTheme()
            val isNotificationEnabled = preferences.getNotificationEnable()

            _settingsState.update {
                it.copy(
                    isDarkTheme = isDarkTheme,
                    isNotificationEnabled = isNotificationEnabled
                )
            }

            getUserData()
        }
    }

    fun setNotificationsEnabled(enabled: Boolean, context: Context) {
        viewModelScope.launch {
            preferences.setNotificationEnable(enabled)

            if (enabled) {
                // Cek permission untuk Android 13+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            scheduleReminder()
                        }
                        else -> {
                            // Beri tahu pengguna untuk mengizinkan permission
                            _settingsState.update {
                                it.copy(isNotificationEnabled = false)
                            }
                        }
                    }
                } else {
                    scheduleReminder()
                }
            } else {
                cancelReminder()
            }
        }
    }

    fun getUserData() = viewModelScope.launch {
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
            _settingsState.update { it.copy(isLoading = true) }

            when (val result = repository.logout()) {
                is Result.Success -> {
                    onLogoutSuccess()
                }
                is Result.Error -> {
                    _settingsState.update {
                        it.copy(
                            error = result.error ?: "Logout failed",
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {
                    _settingsState.update { it.copy(isLoading = true) }
                }
            }

            _settingsState.update { it.copy(isLoading = false) }
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

    private fun scheduleReminder() {
        val reminderRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            DAILY_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            reminderRequest
        )
    }

    private fun cancelReminder() {
        workManager.cancelUniqueWork(DAILY_REMINDER_WORK_NAME)
    }

    fun clearError() {
        _settingsState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _settingsState.update {
            it.copy(success = false)
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