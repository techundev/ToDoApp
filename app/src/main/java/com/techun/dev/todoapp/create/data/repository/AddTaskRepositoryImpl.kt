package com.techun.dev.todoapp.create.data.repository

import com.techun.dev.todoapp.core.database.dao.TaskDao
import com.techun.dev.todoapp.create.data.mapper.toEntity
import com.techun.dev.todoapp.create.domain.model.NewTask
import com.techun.dev.todoapp.create.domain.repository.CreateTaskRepository
import java.time.Clock

class AddTaskRepositoryImpl(
    private val dao: TaskDao,
    private val clock: Clock = Clock.systemDefaultZone()
) : CreateTaskRepository {
    override suspend fun addTask(task: NewTask): Result<Unit> = runCatching {
        val rowId = dao.insert(task.toEntity(now = clock.millis()))
        check(rowId != -1L) { "Insert falló: conflicto o restricción violada" }
    }
}