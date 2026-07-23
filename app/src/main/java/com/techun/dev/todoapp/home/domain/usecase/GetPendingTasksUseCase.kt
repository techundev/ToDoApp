package com.techun.dev.todoapp.home.domain.usecase

import com.techun.dev.todoapp.home.domain.model.HomeResult
import com.techun.dev.todoapp.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetPendingTasksUseCase(private val taskRepository: HomeRepository) {
    operator fun invoke(): Flow<HomeResult> = taskRepository.getPendingTask()
}