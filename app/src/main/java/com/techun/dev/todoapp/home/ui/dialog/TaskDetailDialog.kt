package com.techun.dev.todoapp.home.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.home.domain.model.Task
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TaskDetailDialog(
    task: Task, onDismiss: () -> Unit
) {
    val dateFormatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("h:mm a") }

    AlertDialog(onDismissRequest = onDismiss, icon = {
        Icon(
            imageVector = if (task.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.AccessTime,
            contentDescription = null
        )
    }, title = { ToDoText(text = task.title) }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DetailRow("Fecha", task.date.format(dateFormatter))
            DetailRow("Inicio", task.startTime?.format(timeFormatter) ?: "—")
            DetailRow("Fin", task.endTime?.format(timeFormatter) ?: "—")
            DetailRow("Prioridad", task.priority.name)
            DetailRow("Estado", task.status.name)
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) { ToDoText("Cerrar") }
    })
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        ToDoText(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ToDoText(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
