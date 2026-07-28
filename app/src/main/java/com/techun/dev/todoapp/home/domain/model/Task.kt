package com.techun.dev.todoapp.home.domain.model

data class Task(
    val id: Long,
    val title: String,
    val priority: PriorityStatus,
    val status: TaskStatus,
    val createdAt: String,
    val updatedAt: String,
    val completedAt: Long?,
    val deletedAt: Long?,
    val isCompleted: Boolean
)