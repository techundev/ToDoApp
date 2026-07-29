package com.techun.dev.todoapp.home.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techun.dev.todoapp.core.composables.ToDoText

@Composable
fun ToDoBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp)
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(
            width = 1.dp,
            color = color
        )
    ) {
        ToDoText(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 3.dp
            )
        )
    }
}