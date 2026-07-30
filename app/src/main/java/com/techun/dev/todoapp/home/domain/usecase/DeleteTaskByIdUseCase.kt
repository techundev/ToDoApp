package com.techun.dev.todoapp.home.domain.usecase

import com.techun.dev.todoapp.home.domain.repository.HomeRepository

class DeleteTaskByIdUseCase(private val taskRepository: HomeRepository) {
    suspend operator fun invoke(taskId: Long): Result<Unit> =
        taskRepository.deleteTask(taskId = taskId)
}