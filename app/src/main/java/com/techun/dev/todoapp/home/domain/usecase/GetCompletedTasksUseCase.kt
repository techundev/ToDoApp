package com.techun.dev.todoapp.home.domain.usecase

import com.techun.dev.todoapp.home.domain.repository.HomeRepository

class GetCompletedTasksUseCase(private val taskRepository: HomeRepository) {
    operator fun invoke() = taskRepository.getCompletedTask()
}