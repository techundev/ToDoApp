package com.techun.dev.todoapp.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.ui.composable.ToDoDayTimeline
import com.techun.dev.todoapp.home.ui.composable.ToDoHorizontalCalendar
import com.techun.dev.todoapp.home.ui.composable.ToDoTaskFloatingActionButton
import com.techun.dev.todoapp.home.ui.dialog.DeleteTaskDialog
import com.techun.dev.todoapp.home.ui.dialog.TaskDetailDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(), onCreateNewTask: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        Column(
            modifier = Modifier.statusBarsPadding()
        ) {
            ToDoHorizontalCalendar(
                modifier = Modifier.fillMaxWidth(),
                selectedDate = uiState.selectedDate,
                onDateSelected = { date ->
                    viewModel.onDateSelected(date)
                })
        }
    }, floatingActionButton = {
        ToDoTaskFloatingActionButton { onCreateNewTask() }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            AnimatedContent(
                targetState = uiState.tasks, transitionSpec = {
                    (fadeIn(animationSpec = tween(300)) + scaleIn(
                        initialScale = 0.95f, animationSpec = tween(300)
                    )).togetherWith(fadeOut(animationSpec = tween(150)))
                }, label = "homeScreenPhaseTransition"
            ) { taskState ->
                when (taskState) {
                    is TaskSectionState.Empty -> {
                        HomeContent(
                            onDeleteTask = { task -> viewModel.deleteTask(task) },
                            onComplete = { task -> viewModel.taskToggleCompleted(task) })
                    }

                    is TaskSectionState.Error -> {}
                    is TaskSectionState.Loading -> TaskSectionLoading()
                    is TaskSectionState.Success -> {
                        HomeContent(
                            tasks = taskState.tasks,
                            onDeleteTask = { task -> viewModel.deleteTask(task) },
                            onComplete = { task -> viewModel.taskToggleCompleted(task) })
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    tasks: List<Task>? = emptyList(), onDeleteTask: (Task) -> Unit, //pendiente de implementar
    onComplete: (Task) -> Unit //pendiente de implementar
) {
    var detailTaskId by rememberSaveable { mutableStateOf<Long?>(null) }
    var deleteTaskId by rememberSaveable { mutableStateOf<Long?>(null) }

    val detailTask = tasks?.find { it.id == detailTaskId }
    val deleteTask = tasks?.find { it.id == deleteTaskId }


    ToDoDayTimeline(
        modifier = Modifier.padding(top = 15.dp, start = 16.dp),
        tasks = tasks,
        onTaskClick = { detailTaskId = it.id },
        onTaskLongClick = { deleteTaskId = it.id })

    detailTask?.let { task ->
        TaskDetailDialog(
            task = task, onDismiss = { detailTaskId = null })
    }

    deleteTask?.let { task ->
        DeleteTaskDialog(task = task, onConfirm = {
            onDeleteTask(task)
            deleteTaskId = null
        }, onDismiss = { deleteTaskId = null })
    }
}

@Composable
fun TaskSectionLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(120.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

