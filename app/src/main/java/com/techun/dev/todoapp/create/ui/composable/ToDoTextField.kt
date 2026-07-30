package com.techun.dev.todoapp.create.ui.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.techun.dev.todoapp.core.composables.ToDoText

@Composable
fun ToDoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    shape: Shape = MaterialTheme.shapes.medium,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    singleLine: Boolean = false,
) {
    OutlinedTextField(
        modifier = modifier,
        shape = shape,
        label = {
            ToDoText(
                text = label,
            )
        },
        value = value,
        onValueChange = { onValueChange(it) },
        colors = colors,
        singleLine = singleLine
    )
}