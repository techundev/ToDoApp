package com.techun.dev.todoapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.domain.model.TaskStatus
import com.techun.dev.todoapp.home.domain.usecase.DeleteTaskByIdUseCase
import com.techun.dev.todoapp.home.domain.usecase.ObserveTasksByDateUseCase
import com.techun.dev.todoapp.home.domain.usecase.ToggleTaskCompletedUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate


sealed interface TaskSectionState {
    data object Loading : TaskSectionState
    data object Empty : TaskSectionState
    data class Success(val tasks: List<Task>) : TaskSectionState
    data class Error(val message: String) : TaskSectionState
}

data class HomeUiState(
    val tasks: TaskSectionState = TaskSectionState.Loading,
    val selectedDate: LocalDate = LocalDate.now()
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    observeTasksByDate: ObserveTasksByDateUseCase,
    private val toggleTaskCompletedUseCase: ToggleTaskCompletedUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val completionOverrides = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    private val pendingDeletions = MutableStateFlow<Set<Long>>(emptySet())

    private val tasksForDate: Flow<List<Task>> =
        selectedDate.flatMapLatest { date -> observeTasksByDate(date) }

    val uiState: StateFlow<HomeUiState> = combine(
        selectedDate, tasksForDate, completionOverrides, pendingDeletions
    ) { date, tasks, overrides, deletions ->
        val resolved = tasks.filterNot { it.id in deletions }.map { t ->
            overrides[t.id]?.let { t.copy(status = if (it) TaskStatus.COMPLETED else TaskStatus.PENDING) }
                ?: t
        }

        HomeUiState(
            selectedDate = date,
            tasks = resolved.filter { !it.isCompleted }.toSectionState()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun onDateSelected(date: LocalDate) {
        selectedDate.value = date
    }

    private val toggleJobs = mutableMapOf<Long, Job>()
    fun taskToggleCompleted(task: Task) {
        val optimisticValue = !(completionOverrides.value[task.id] ?: task.isCompleted)
        completionOverrides.update { it + (task.id to optimisticValue) }

        toggleJobs[task.id]?.cancel()
        toggleJobs[task.id] = viewModelScope.launch {
            toggleTaskCompletedUseCase(task).onFailure {
                completionOverrides.update { current -> current - task.id }
            }.onSuccess {
                completionOverrides.update { current -> current - task.id }
            }
            toggleJobs.remove(task.id)
        }
    }

    fun deleteTask(task: Task) {
        toggleJobs[task.id]?.cancel()
        toggleJobs.remove(task.id)
        pendingDeletions.update { it + task.id }

        viewModelScope.launch {
            deleteTaskByIdUseCase(taskId = task.id).onFailure {
                pendingDeletions.update { it - task.id }
            }.onSuccess {
                completionOverrides.update { current -> current - task.id }
            }
        }
    }
}

private fun List<Task>.toSectionState() =
    if (isEmpty()) TaskSectionState.Empty else TaskSectionState.Success(this)