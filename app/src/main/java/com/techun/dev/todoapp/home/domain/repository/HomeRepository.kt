package com.techun.dev.todoapp.home.domain.repository

import com.techun.dev.todoapp.home.domain.model.HomeResult
import com.techun.dev.todoapp.home.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HomeRepository {
//    fun getPendingTask(): Flow<HomeResult>
//    fun getCompletedTask(): Flow<HomeResult>

    fun observeTasksByDate(day: LocalDate): Flow<List<Task>>
    suspend fun updateTaskCompletion(taskId: Long, isCompleted: Boolean): Result<Unit>
    suspend fun deleteTask(taskId: Long): Result<Unit>
}