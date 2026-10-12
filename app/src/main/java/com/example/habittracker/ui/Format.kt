package com.example.habittracker.ui

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

fun formatValue(value: Double, unit: String): String {
    val number = DecimalFormat("#,##0.##").format(value)
    return if (unit.isBlank()) number else "$number $unit"
}

fun formatDate(date: LocalDate, today: LocalDate = LocalDate.now()): String = when (date) {
    today -> "Today"
    today.minusDays(1) -> "Yesterday"
    else -> date.format(dateFormatter)
}

/** Accepts both "72.4" and "72,4". */
fun parseValue(text: String): Double? = text.trim().replace(',', '.').toDoubleOrNull()
