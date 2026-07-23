package com.techun.dev.todoapp.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techun.dev.todoapp.home.domain.model.HomeResult
import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.domain.usecase.GetCompletedTasksUseCase
import com.techun.dev.todoapp.home.domain.usecase.GetPendingTasksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn


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
    getCompletedTasksUseCase: GetCompletedTasksUseCase
) : ViewModel() {
    private val pendingState: Flow<TaskSectionState> =
        getPendingTasksUseCase()
            .map { it.toTaskSectionState() }
            .onStart { emit(TaskSectionState.Loading) }

    private val completedState: Flow<TaskSectionState> =
        getCompletedTasksUseCase()
            .map { it.toTaskSectionState() }
            .onStart { emit(TaskSectionState.Loading) }

    val uiState: StateFlow<HomeUiState> =
        combine(pendingState, completedState) { pending, completed ->
            HomeUiState(pending = pending, completed = completed)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )
}

private fun HomeResult.toTaskSectionState() =
    when (this) {
        is HomeResult.Success -> TaskSectionState.Success(tasks)
        HomeResult.Empty -> TaskSectionState.Empty
        is HomeResult.Error -> TaskSectionState.Error(message)
    }