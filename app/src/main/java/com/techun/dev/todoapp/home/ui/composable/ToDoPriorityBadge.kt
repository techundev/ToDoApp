package com.techun.dev.todoapp.home.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.techun.dev.todoapp.home.domain.model.PriorityStatus

@Composable
fun ToDoPriorityBadge(
    priority: PriorityStatus,
    modifier: Modifier = Modifier
) {
    ToDoBadge(
        modifier = modifier,
        text = priority.displayName,
        color = priority.color()
    )
}

private fun PriorityStatus.color(): Color = when (this) {
    PriorityStatus.LOW -> Color(0xFF64B5F6)
    PriorityStatus.MEDIUM -> Color(0xFFBA68C8)
    PriorityStatus.HIGH -> Color(0xFFEF5350)
}
