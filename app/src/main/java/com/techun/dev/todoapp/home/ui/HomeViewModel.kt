package com.techun.dev.todoapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techun.dev.todoapp.home.domain.model.HomeResult
import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.domain.usecase.GetCompletedTasksUseCase
import com.techun.dev.todoapp.home.domain.usecase.GetPendingTasksUseCase
import com.techun.dev.todoapp.home.domain.usecase.ToggleTaskCompletedUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface TaskSectionState {
    data object Loading : TaskSectionState
    data object Empty : TaskSectionState
    data class Success(val tasks: List<Task>) : TaskSectionState
    data class Error(val message: String) : TaskSectionState
}

data class HomeUiState(
    val pending: TaskSectionState = TaskSectionState.Loading,
    val completed: TaskSectionState = TaskSectionState.Loading
)

class HomeViewModel(
    getPendingTasksUseCase: GetPendingTasksUseCase,
    getCompletedTasksUseCase: GetCompletedTasksUseCase,
    private val toggleTaskCompletedUseCase: ToggleTaskCompletedUseCase
) : ViewModel() {
    private val pendingState: Flow<TaskSectionState> =
        getPendingTasksUseCase()
            .map { it.toTaskSectionState() }
            .onStart { emit(TaskSectionState.Loading) }

    private val completedState: Flow<TaskSectionState> =
        getCompletedTasksUseCase()
            .map { it.toTaskSectionState() }
            .onStart { emit(TaskSectionState.Loading) }

    private val completionOverrides = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val uiState: StateFlow<HomeUiState> =
        combine(
            pendingState,
            completedState,
            completionOverrides
        ) { pending, completed, overrides ->
            val (newPending, newCompleted) = reconcileSections(pending, completed, overrides)
            HomeUiState(
                pending = newPending,
                completed = newCompleted
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    private fun reconcileSections(
        pending: TaskSectionState,
        completed: TaskSectionState,
        overrides: Map<Long, Boolean>
    ): Pair<TaskSectionState, TaskSectionState> {
        val pendingTask = (pending as? TaskSectionState.Success)?.tasks.orEmpty()
        val completedTask = (completed as? TaskSectionState.Success)?.tasks.orEmpty()

        if (pendingTask.isEmpty() && completedTask.isEmpty()) {
            return pending to completed
        }

        val merged = linkedMapOf<Long, Task>()
        pendingTask.forEach { merged[it.id] = it }
        completedTask.forEach { merged[it.id] = it }

        val resolved = merged.values.map { task ->
            val override = overrides[task.id]
            if (override != null && override != task.isCompleted) task.copy(isCompleted = override) else task
        }

        val newPending = resolved.filter { !it.isCompleted }
        val newCompleted = resolved.filter { it.isCompleted }

        val newPendingState = when {
            pending is TaskSectionState.Loading && newPending.isEmpty() -> pending
            pending is TaskSectionState.Error -> pending
            newPending.isEmpty() -> TaskSectionState.Empty
            else -> TaskSectionState.Success(newPending)
        }


        val newCompletedState = when {
            completed is TaskSectionState.Loading && newCompleted.isEmpty() -> completed
            completed is TaskSectionState.Error -> completed
            newCompleted.isEmpty() -> TaskSectionState.Empty
            else -> TaskSectionState.Success(newCompleted)
        }

        return newPendingState to newCompletedState
    }

    fun taskToggleCompleted(task: Task) {
        val optimisticValue = !(completionOverrides.value[task.id] ?: task.isCompleted)
        completionOverrides.update { it + (task.id to optimisticValue) }

        viewModelScope.launch {
            toggleTaskCompletedUseCase(task)
                .onFailure {
                    completionOverrides.update { current -> current - task.id }
                }
                .onSuccess {
                    completionOverrides.update { current -> current - task.id }
                }
        }
    }
}


private fun HomeResult.toTaskSectionState() =
    when (this) {
        is HomeResult.Success -> TaskSectionState.Success(tasks)
        HomeResult.Empty -> TaskSectionState.Empty
        is HomeResult.Error -> TaskSectionState.Error(message)
    }