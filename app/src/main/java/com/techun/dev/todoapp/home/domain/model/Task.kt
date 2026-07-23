package com.techun.dev.todoapp.home.domain.model

data class Task(
    val id: Long,
    val title: String,
    val priority: PriorityStatus,
    val status: TaskStatus,
    val createdAt: Long,
    val updatedAt: Long?,
    val completedAt: Long?,
    val deletedAt: Long?
)