package com.techun.dev.todoapp.create.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techun.dev.todoapp.create.domain.model.NewTask
import com.techun.dev.todoapp.create.domain.usecase.AddTaskUseCase
import com.techun.dev.todoapp.home.domain.model.PriorityStatus
import com.techun.dev.todoapp.home.domain.model.TaskStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

data class CreateTaskUiState(
    val title: String = "", val priority: Float = 0f, val isLoading: Boolean = false
)

sealed interface CreateTaskEvent {
    data object TaskCreated : CreateTaskEvent
    data class ShowError(val message: String) : CreateTaskEvent
}

class CreateTaskViewModel(private val addTaskUseCase: AddTaskUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateTaskUiState())
    val uiState: StateFlow<CreateTaskUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateTaskEvent>()
    val events: SharedFlow<CreateTaskEvent> = _events.asSharedFlow()


    private fun mockTask() = NewTask(
        title = "Tarea de prueba",
        priority = PriorityStatus.MEDIUM,
        status = TaskStatus.PENDING,
        date = LocalDate.now(),
        startTime = LocalTime.of(9, 0),
        endTime = LocalTime.of(10, 30)
    )

    fun onTitleChanged(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onPriorityChanged(newPriority: Float) {
        _uiState.update { it.copy(priority = newPriority) }
    }

    fun onDoneClicked() {
//        val current = _uiState.value
//
//        if (current.title.isBlank()) {
//            viewModelScope.launch {
//                _events.emit(CreateTaskEvent.ShowError("El título es obligatorio"))
//            }
//            return
//        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

//            val task = Task(
//                id = 0L,
//                title = current.title,
//                priority = current.priority.toPriorityStatus(),
//                status = TaskStatus.PENDING,
//                createdAt = "",
//                updatedAt = "",
//                completedAt = null,
//                deletedAt = null,
//            )

            addTaskUseCase(mockTask()).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false, title = "", priority = 0f
                    )
                }
                _events.emit(CreateTaskEvent.TaskCreated)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                _events.emit(
                    CreateTaskEvent.ShowError(
                        throwable.message ?: "Error al guardar la tarea"
                    )
                )
            }
        }
    }
}

private fun Float.toPriorityStatus(): PriorityStatus = when (roundToInt()) {
    0 -> PriorityStatus.LOW
    1 -> PriorityStatus.MEDIUM
    else -> PriorityStatus.HIGH
}