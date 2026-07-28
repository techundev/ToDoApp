package com.techun.dev.todoapp.home.data.mapper

import com.techun.dev.todoapp.home.data.local.entity.TaskEntity
import com.techun.dev.todoapp.home.data.utils.toDateString
import com.techun.dev.todoapp.home.domain.model.Task

fun TaskEntity.toDomain() = Task(
    id = id,
    title = title,
    priority = priority,
    status = status,
    createdAt = createdAt.toDateString(),
    updatedAt = updatedAt.toDateString(),
    completedAt = completedAt,
    deletedAt = deletedAt,
    isCompleted = isCompleted
)