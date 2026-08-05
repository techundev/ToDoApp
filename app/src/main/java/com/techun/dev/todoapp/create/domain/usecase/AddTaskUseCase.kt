package com.techun.dev.todoapp.create.domain.usecase

import com.techun.dev.todoapp.create.domain.repository.CreateTaskRepository
import com.techun.dev.todoapp.home.domain.model.Task

class AddTaskUseCase(private val repository: CreateTaskRepository) {
    suspend operator fun invoke(task: Task): Result<Unit> = repository.addTask(task)
}