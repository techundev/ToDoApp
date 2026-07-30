package com.techun.dev.todoapp.home.data.repository

import com.techun.dev.todoapp.home.data.local.dao.TaskDao
import com.techun.dev.todoapp.home.data.mapper.toDomain
import com.techun.dev.todoapp.home.domain.model.HomeResult
import com.techun.dev.todoapp.home.domain.model.TaskStatus
import com.techun.dev.todoapp.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val dao: TaskDao) : HomeRepository {
    override fun getPendingTask(): Flow<HomeResult> = dao.getPendingTasks().map { entities ->
        val tasks = entities.map { it.toDomain() }

        if (tasks.isEmpty()) {
            HomeResult.Empty
        } else {
            HomeResult.Success(tasks)
        }
    }.catch {
        emit(HomeResult.Error(it.message ?: "Error desconocido"))
    }

    override fun getCompletedTask(): Flow<HomeResult> = dao.getCompletedTasks().map { entities ->
        val tasks = entities.map { it.toDomain() }

        if (tasks.isEmpty()) {
            HomeResult.Empty
        } else {
            HomeResult.Success(tasks)
        }
    }.catch {
        emit(HomeResult.Error(it.message ?: "Error desconocido"))
    }

    override suspend fun updateTaskCompletion(
        taskId: Long, isCompleted: Boolean
    ): Result<Unit> = runCatching {
        val now = System.currentTimeMillis()
        val rowsAffected = dao.updateCompletion(
            id = taskId,
            status = if (isCompleted) TaskStatus.COMPLETED else TaskStatus.PENDING,
            isCompleted = isCompleted,
            completedAt = if (isCompleted) now else null,
            updatedAt = now
        )
        check(rowsAffected > 0) { "Task with id $taskId not found" }
    }

    override suspend fun deleteTask(taskId: Long): Result<Unit> = runCatching {
        val deleteData = dao.deleteById(taskId)
        check(deleteData > 0) { "Task with id $taskId not found" }
    }
}