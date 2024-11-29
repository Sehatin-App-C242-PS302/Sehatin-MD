package com.c242_ps302.sehatin.presentation.utils

import android.util.Log
import com.c242_ps302.sehatin.data.repository.Result
import kotlinx.coroutines.flow.Flow

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
