package com.techun.dev.todoapp.home.domain.usecase

import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.domain.repository.HomeRepository

class ToggleTaskCompletedUseCase(private val taskRepository: HomeRepository) {
    suspend operator fun invoke(task: Task): Result<Unit> =
        taskRepository.updateTaskCompletion(
            taskId = task.id,
            isCompleted = !task.isCompleted
        )
}