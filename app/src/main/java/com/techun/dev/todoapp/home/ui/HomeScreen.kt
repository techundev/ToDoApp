package com.techun.dev.todoapp.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
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
import com.techun.dev.todoapp.home.ui.composable.ToDoTaskItem
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
            HomeContent(
                uiState = uiState,
                onDelete = {},
                onComplete = { task -> viewModel.taskToggleCompleted(task) })
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onDelete: (Task) -> Unit,
    onComplete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        taskSection(
            key = "pending",
            title = "Pendientes",
            state = uiState.pending,
            onDelete = onDelete,
            onComplete = onComplete
        )

        item(key = "divider") {
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }

        taskSection(
            key = "completed",
            title = "Completadas",
            state = uiState.completed,
            onDelete = onDelete,
            onComplete = onComplete
        )
    }
}

private fun LazyListScope.taskSection(
    key: String,
    title: String,
    state: TaskSectionState,
    onDelete: (Task) -> Unit,
    onComplete: (Task) -> Unit
) {
    item(key = "${key}_header", contentType = "header") {
        ToDoText(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    when (state) {
        TaskSectionState.Loading -> item(key = "${key}_loading", contentType = "loading") {
            TaskSectionLoading()
        }

        TaskSectionState.Empty -> item(key = "${key}_empty", contentType = "empty") {
            TaskSectionEmpty()
        }

        is TaskSectionState.Error -> item(key = "${key}_error", contentType = "error") {
            TaskSectionError(message = state.message)
        }

        is TaskSectionState.Success -> items(
            items = state.tasks,
            key = { "${key}_${it.id}" },
            contentType = { "task" }) { task ->
            ToDoTaskItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .animateItem() ,
                task = task,
                onDelete = onDelete,
                onComplete = onComplete
            )
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
