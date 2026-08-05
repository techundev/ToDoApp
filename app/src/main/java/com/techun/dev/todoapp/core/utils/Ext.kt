package com.techun.dev.todoapp.core.utils

import androidx.compose.ui.graphics.Color
import com.techun.dev.todoapp.home.domain.model.PriorityStatus

fun PriorityStatus.color(): Color = when (this) {
    PriorityStatus.LOW -> Color(0xFF64B5F6)
    PriorityStatus.MEDIUM -> Color(0xFFBA68C8)
    PriorityStatus.HIGH -> Color(0xFFEF5350)
}

fun Float.color(): Color = when (this) {
    0f -> Color(0xFF64B5F6)
    1f -> Color(0xFFBA68C8)
    2f -> Color(0xFFEF5350)
    else -> Color(0xFFEF5350)
}
