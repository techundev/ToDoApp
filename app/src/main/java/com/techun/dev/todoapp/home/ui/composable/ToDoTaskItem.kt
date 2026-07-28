package com.techun.dev.todoapp.home.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.home.domain.model.PriorityStatus
import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.domain.model.TaskStatus


@Preview(showBackground = true)
@Composable
fun ToDoTaskItemPreview() {
    val task = Task(
        id = 0,
        title = "Read a book",
        priority = PriorityStatus.HIGH,
        status = TaskStatus.PENDING,
        createdAt = "7/28/2026",
        updatedAt = "7/28/2026",
        completedAt = null,
        deletedAt = null,
        isCompleted = false
    )

    ToDoTaskItem(task = task, onDelete = {}, onComplete = {})
}

@Composable
fun ToDoTaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onDelete: (Task) -> Unit,
    onComplete: (Task) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            IconButton(
                onClick = { onDelete(task) },
                modifier = Modifier.size(30.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.LightGray
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ToDoText(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                ToDoText(text = task.priority.name)
            }

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onComplete(task) }
            )
        }
    }
}