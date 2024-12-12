package com.c242_ps302.sehatin.ui.components.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay

enum class ToastType {
    SUCCESS,
    ERROR,
    INFO
}

@Composable
fun SehatinToast(
    message: String,
    type: ToastType = ToastType.INFO,
    duration: Long = 2000L,
    onDismiss: () -> Unit,
    placement: Alignment = Alignment.BottomCenter,
) {
    var isVisible by remember { mutableStateOf(true) }

    val backgroundColor = when (type) {
        ToastType.SUCCESS -> Color(0xFF66BB6A)
        ToastType.ERROR -> Color(0xFFF44336)
        ToastType.INFO -> Color(0xFF2196F3)
    }

    val icon = when (type) {
        ToastType.SUCCESS -> Icons.Filled.Check
        ToastType.ERROR -> Icons.Filled.Warning
        ToastType.INFO -> Icons.Filled.Info
    }

    LaunchedEffect(key1 = true) {
        delay(duration)
        isVisible = false
        onDismiss()
    }

    if (isVisible) {
        Popup(
            alignment = placement,
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { -it }
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it }
                ) + fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 80.dp)
                        .wrapContentSize(),
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 4.dp,
                    color = backgroundColor
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            text = message,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}