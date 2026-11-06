package com.techun.dev.todoapp.create.domain.usecase

import com.techun.dev.todoapp.create.domain.model.NewTask
import com.techun.dev.todoapp.create.domain.repository.CreateTaskRepository

class AddTaskUseCase(private val repository: CreateTaskRepository) {
    suspend operator fun invoke(task: NewTask): Result<Unit> = repository.addTask(task)
}