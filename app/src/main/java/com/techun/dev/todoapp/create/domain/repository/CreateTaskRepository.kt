package com.techun.dev.todoapp.create.domain.repository

import com.techun.dev.todoapp.home.domain.model.Task

interface CreateTaskRepository {
    suspend fun addTask(task: Task): Result<Unit>
}