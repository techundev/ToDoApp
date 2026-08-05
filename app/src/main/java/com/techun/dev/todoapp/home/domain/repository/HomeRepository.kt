package com.techun.dev.todoapp.home.domain.repository

import com.techun.dev.todoapp.home.domain.model.HomeResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getPendingTask(): Flow<HomeResult>
    fun getCompletedTask(): Flow<HomeResult>
    suspend fun updateTaskCompletion(taskId: Long, isCompleted: Boolean): Result<Unit>
    suspend fun deleteTask(taskId: Long): Result<Unit>
}