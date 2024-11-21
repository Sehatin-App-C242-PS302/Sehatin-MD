package com.c242_ps302.sehatin.data.utils

import android.annotation.SuppressLint
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi")
fun formatCardDate(isoDate: String): String {
    val zonedDateTime = ZonedDateTime.parse(isoDate)

    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())

    return zonedDateTime.format(formatter)
}