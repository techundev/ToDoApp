package com.techun.dev.todoapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.techun.dev.todoapp.core.database.entity.TaskEntity
import com.techun.dev.todoapp.home.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query(
        """
        SELECT * FROM task
        WHERE task_date = :epochDay
        ORDER BY start_minute IS NULL, start_minute ASC, created_at DESC
    """
    )
    fun observeByDate(epochDay: Long): Flow<List<TaskEntity>>

    // Opcional: marcar en el calendario los días que tienen tareas
    @Query(
        """
        SELECT DISTINCT task_date FROM task
        WHERE task_date BETWEEN :fromEpochDay AND :toEpochDay
    """
    )
    fun observeDatesWithTasks(fromEpochDay: Long, toEpochDay: Long): Flow<List<Long>>

    @Insert
    suspend fun insert(task: TaskEntity): Long

    @Query(
        """
        UPDATE task
        SET status = :status, completed_at = :completedAt, updated_at = :updatedAt
        WHERE id = :id
    """
    )
    suspend fun updateStatus(id: Long, status: TaskStatus, completedAt: Long?, updatedAt: Long): Int

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteById(id: Long): Int

//    @Query(
//        """
//        SELECT * FROM task
//        WHERE status= 'PENDING'
//        ORDER BY created_at DESC"""
//    )
//    fun getPendingTasks(): Flow<List<TaskEntity>>
//
//    @Query(
//        """
//        SELECT * FROM task
//        WHERE status= 'COMPLETED'
//        ORDER BY created_at DESC"""
//    )
//    fun getCompletedTasks(): Flow<List<TaskEntity>>
//
//    @Query("SELECT COUNT(*) FROM task")
//    suspend fun count(): Int
//
//    @Insert
//    suspend fun insertAll(tasks: List<TaskEntity>)
//
//    @Insert
//    suspend fun insertTask(tasks: TaskEntity): Long

//    @Query(
//        """
//        UPDATE task
//        SET status = :status,
//            is_completed = :isCompleted,
//            completed_at = :completedAt,
//            updated_at = :updatedAt
//        WHERE id = :id
//        """
//    )
//    suspend fun updateCompletion(
//        id: Long, status: TaskStatus, isCompleted: Boolean, completedAt: Long?, updatedAt: Long
//    ): Int

//    @Query("DELETE FROM task WHERE id = :id")
//    suspend fun deleteById(id: Long): Int
}