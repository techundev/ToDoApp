package com.techun.dev.todoapp.create.data.repository

import com.techun.dev.todoapp.core.database.local.dao.TaskDao
import com.techun.dev.todoapp.create.data.mapper.toEntity
import com.techun.dev.todoapp.create.domain.repository.CreateTaskRepository
import com.techun.dev.todoapp.home.domain.model.Task

class AddTaskRepositoryImpl(private val dao: TaskDao) : CreateTaskRepository {
    override suspend fun addTask(task: Task): Result<Unit> = runCatching {
        val rowId = dao.insertTask(task.toEntity())
        check(rowId != -1L) { "Insert falló: conflicto o restricción violada" }
    }
}