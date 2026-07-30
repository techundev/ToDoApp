package com.techun.dev.todoapp.home.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techun.dev.todoapp.core.utils.color
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