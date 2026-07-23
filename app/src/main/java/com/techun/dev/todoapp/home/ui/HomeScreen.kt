package com.techun.dev.todoapp.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.home.domain.model.Task
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ToDoText(text = "ToDo")
            TaskSection(title = "Pendientes", state = uiState.pending)
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            TaskSection(title = "Completadas", state = uiState.completed)
        }
    }
}

@Composable
private fun TaskSection(title: String, state: TaskSectionState) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        ToDoText(text = title, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        when (state) {
            TaskSectionState.Loading -> TaskSectionLoading()
            TaskSectionState.Empty -> TaskSectionEmpty()
            is TaskSectionState.Success -> TaskList(tasks = state.tasks)
            is TaskSectionState.Error -> TaskSectionError(message = state.message)
        }
    }
}

@Composable
fun TaskSectionLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TaskSectionEmpty() {
    ToDoText(
        text = "No hay tareas",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}


@Composable
private fun TaskSectionError(message: String) {
    ToDoText(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
private fun TaskList(tasks: List<Task>) {
    Column {
        tasks.forEach { task ->
            Column(modifier = Modifier.fillMaxWidth()) {
                ToDoText(text = task.title)
                ToDoText(text = task.priority.name)
                ToDoText(text = task.status.name)
                ToDoText(text = task.createdAt)
            }

        }
    }
}
