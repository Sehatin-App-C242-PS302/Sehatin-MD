package com.c242_ps302.sehatin.ui.utils

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageChangeHelper @Inject constructor(): ViewModel() {
    fun changeLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)?.applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(languageCode)
            )
        }

        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getLanguageCode(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales.takeIf { it.size() > 0 }
                ?.get(0)?.language ?: "en"
        } else {
            AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
        }
    }
}