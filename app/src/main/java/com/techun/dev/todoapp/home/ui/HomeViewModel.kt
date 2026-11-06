package com.techun.dev.todoapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techun.dev.todoapp.home.domain.model.HomeResult
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
    val pending: TaskSectionState = TaskSectionState.Loading,
    val completed: TaskSectionState = TaskSectionState.Loading,
    val selectedDate: LocalDate = LocalDate.now()
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
//    getPendingTasksUseCase: GetPendingTasksUseCase,
//    getCompletedTasksUseCase: GetCompletedTasksUseCase,
    observeTasksByDate: ObserveTasksByDateUseCase,
    private val toggleTaskCompletedUseCase: ToggleTaskCompletedUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase
) : ViewModel() {
//    private val pendingState: Flow<TaskSectionState> =
//        getPendingTasksUseCase().map { it.toTaskSectionState() }
//            .onStart { emit(TaskSectionState.Loading) }
//
//    private val completedState: Flow<TaskSectionState> =
//        getCompletedTasksUseCase().map { it.toTaskSectionState() }
//            .onStart { emit(TaskSectionState.Loading) }

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val completionOverrides = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    private val pendingDeletions = MutableStateFlow<Set<Long>>(emptySet())

    private val tasksForDate: Flow<List<Task>> =
        selectedDate.flatMapLatest { date -> observeTasksByDate(date) }

//    val uiState: StateFlow<HomeUiState> = combine(
//        pendingState, completedState, completionOverrides, pendingDeletions
//    ) { pending, completed, overrides, deletions ->
//        val (newPending, newCompleted) = reconcileSections(pending, completed, overrides, deletions)
//        HomeUiState(
//            pending = newPending, completed = newCompleted
//        )
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = HomeUiState()
//    )

    val uiState: StateFlow<HomeUiState> = combine(
        selectedDate, tasksForDate, completionOverrides, pendingDeletions
    ) { date, tasks, overrides, deletions ->
        val resolved = tasks.filterNot { it.id in deletions }.map { t ->
            overrides[t.id]?.let { t.copy(status = if (it) TaskStatus.COMPLETED else TaskStatus.PENDING) }
                ?: t
        }

        HomeUiState(
            selectedDate = date,
            pending = resolved.filter { !it.isCompleted }.toSectionState(),
            completed = resolved.filter { it.isCompleted }.toSectionState()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun onDateSelected(date: LocalDate) {
        selectedDate.value = date
    }

//    private fun reconcileSections(
//        pending: TaskSectionState,
//        completed: TaskSectionState,
//        overrides: Map<Long, Boolean>,
//        deletions: Set<Long>
//    ): Pair<TaskSectionState, TaskSectionState> {
//        val pendingTask = (pending as? TaskSectionState.Success)?.tasks.orEmpty()
//        val completedTask = (completed as? TaskSectionState.Success)?.tasks.orEmpty()
//
//        if (pendingTask.isEmpty() && completedTask.isEmpty()) {
//            return pending to completed
//        }
//
//        val merged = linkedMapOf<Long, Task>()
//        pendingTask.forEach { merged[it.id] = it }
//        completedTask.forEach { merged[it.id] = it }
//
//        val resolved = merged.values.filterNot { it.id in deletions }.map { task ->
//            val override = overrides[task.id]
//            if (override != null && override != task.isCompleted) task.copy(isCompleted = override) else task
//        }
//
//        val newPending = resolved.filter { !it.isCompleted }
//        val newCompleted = resolved.filter { it.isCompleted }
//
//        val newPendingState = when {
//            pending is TaskSectionState.Loading && newPending.isEmpty() -> pending
//            pending is TaskSectionState.Error -> pending
//            newPending.isEmpty() -> TaskSectionState.Empty
//            else -> TaskSectionState.Success(newPending)
//        }
//
//
//        val newCompletedState = when {
//            completed is TaskSectionState.Loading && newCompleted.isEmpty() -> completed
//            completed is TaskSectionState.Error -> completed
//            newCompleted.isEmpty() -> TaskSectionState.Empty
//            else -> TaskSectionState.Success(newCompleted)
//        }
//
//        return newPendingState to newCompletedState
//    }

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

private fun HomeResult.toTaskSectionState() = when (this) {
    is HomeResult.Success -> TaskSectionState.Success(tasks)
    HomeResult.Empty -> TaskSectionState.Empty
    is HomeResult.Error -> TaskSectionState.Error(message)
}

private fun List<Task>.toSectionState() =
    if (isEmpty()) TaskSectionState.Empty else TaskSectionState.Success(this)