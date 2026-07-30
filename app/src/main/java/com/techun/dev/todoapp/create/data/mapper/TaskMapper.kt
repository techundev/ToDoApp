package com.techun.dev.todoapp.create.data.mapper

import com.techun.dev.todoapp.core.database.local.entity.TaskEntity
import com.techun.dev.todoapp.home.domain.model.Task

fun Task.toEntity() = TaskEntity(
    title = title,
    priority = priority,
    status = status,
    createdAt = System.currentTimeMillis()
)