package com.techun.dev.todoapp.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.techun.dev.todoapp.home.domain.model.PriorityStatus
import com.techun.dev.todoapp.home.domain.model.TaskStatus

@Entity(
    tableName = "task",
    indices = [Index(value = ["task_date", "status"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val priority: PriorityStatus,
    val status: TaskStatus,
    @ColumnInfo(name = "task_date") val taskDate: Long,
    @ColumnInfo(name = "start_minute") val startMinute: Int? = null,
    @ColumnInfo(name = "end_minute") val endMinute: Int? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long? = null,
    @ColumnInfo(name = "completed_at") val completedAt: Long? = null
)