package com.c242_ps302.sehatin.ui.utils

import android.util.Log
import com.c242_ps302.sehatin.data.repository.Result
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

suspend fun <T> Flow<Result<T>>.collectAndHandle(
    onError: (String) -> Unit = { error ->
        Log.e("collectAndHandle", "collectAndHandle : error $error")
    },
    onLoading: () -> Unit = {},
    stateReducer: (T) -> Unit,
) {
    collect { result ->
        when (result) {
            is Result.Error -> {
                onError(result.error)
            }

            is Result.Loading -> {
                onLoading()
            }

            is Result.Success -> {
                stateReducer(result.data)
            }
        }
    }
}

fun String.formatHistoryCardDate(locale: Locale = Locale.getDefault()): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("EEEE, dd MMM yyyy", locale)

        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: "Invalid Date"
    } catch (e: Exception) {
        "Invalid Date"
    }
}