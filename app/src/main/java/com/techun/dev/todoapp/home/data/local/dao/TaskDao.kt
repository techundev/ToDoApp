package com.techun.dev.todoapp.home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.techun.dev.todoapp.home.data.local.entity.TaskEntity
import com.techun.dev.todoapp.home.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query(
        """
        SELECT * FROM task 
        WHERE status= 'PENDING' 
        ORDER BY created_at DESC"""
    )
    fun getPendingTasks(): Flow<List<TaskEntity>>

    @Query(
        """
        SELECT * FROM task 
        WHERE status= 'COMPLETED' 
        ORDER BY created_at DESC"""
    )
    fun getCompletedTasks(): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM task")
    suspend fun count(): Int

    @Insert
    suspend fun insertAll(tasks: List<TaskEntity>)

    @Query(
        """
        UPDATE task
        SET status = :status,
            is_completed = :isCompleted,
            completed_at = :completedAt,
            updated_at = :updatedAt
        WHERE id = :id
        """
    )
    suspend fun updateCompletion(
        id: Long,
        status: TaskStatus,
        isCompleted: Boolean,
        completedAt: Long?,
        updatedAt: Long
    ): Int
}