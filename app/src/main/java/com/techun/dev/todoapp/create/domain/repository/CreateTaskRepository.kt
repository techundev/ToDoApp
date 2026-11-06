package com.techun.dev.todoapp.create.domain.repository

import com.techun.dev.todoapp.create.domain.model.NewTask

interface CreateTaskRepository {
    suspend fun addTask(task: NewTask): Result<Unit>
}