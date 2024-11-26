package com.c242_ps302.sehatin.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val timestamp = this.toLongOrNull() ?: return "Invalid Date"
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("EEEE, dd MMM yyyy", locale)
    return formatter.format(date)
}