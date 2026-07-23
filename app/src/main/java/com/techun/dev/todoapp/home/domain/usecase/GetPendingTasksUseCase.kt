package com.techun.dev.todoapp.home.domain.usecase

import com.techun.dev.todoapp.home.domain.repository.HomeRepository

class GetPendingTasksUseCase(private val taskRepository: HomeRepository) {
    operator fun invoke() = taskRepository.getPendingTask()
}