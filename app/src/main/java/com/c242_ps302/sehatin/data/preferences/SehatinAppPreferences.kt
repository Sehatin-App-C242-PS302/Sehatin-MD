package com.c242_ps302.sehatin.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SehatinAppPreferences")

@Singleton
class SehatinAppPreferences @Inject constructor(
    private val context: Context,
) {
    companion object {
        private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        private val IS_NOTIFICATION_ENABLE = booleanPreferencesKey("is_notification_enable")
        private val TOKEN = stringPreferencesKey("token")
    }

    fun getToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun setToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    fun getDarkThemeFlow(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_DARK_THEME] ?: false
        }
    }

    fun getNotificationFlow(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_NOTIFICATION_ENABLE] ?: false
        }
    }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDarkTheme
        }
    }

    suspend fun getDarkTheme(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[IS_DARK_THEME] ?: false
    }

    suspend fun setNotificationEnable(isNotificationEnable: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_NOTIFICATION_ENABLE] = isNotificationEnable
        }
    }

    suspend fun getNotificationEnable(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[IS_NOTIFICATION_ENABLE] ?: false
    }
}