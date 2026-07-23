package com.techun.dev.todoapp.home.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.techun.dev.todoapp.R
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.home.domain.model.Task

@Composable
fun ToDoTaskItem(
    modifier: Modifier = Modifier, task: Task, onDelete: (Task) -> Unit, onComplete: (Task) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null
            )

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                ToDoText(text = task.title)
                ToDoText(text = task.priority.name)
            }

            Icon(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null
            )
        }
    }
}