package com.c242_ps302.sehatin.presentation.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

val searchKeywords: List<String> = listOf(
    "fitness",
    "diet",
    "exercise",
    "sleep",
    "stress",
    "hydration",
    "mental",
    "nutrition",
    "wellness",
    "energy",
    "doctor",
    "weight",
    "vitamin",
    "medicine",
    "yoga",
    "balance",
    "active",
    "heart",
    "focus"
)


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