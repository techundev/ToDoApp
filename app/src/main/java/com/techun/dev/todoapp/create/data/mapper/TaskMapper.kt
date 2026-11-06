package com.techun.dev.todoapp.create.data.mapper

import com.techun.dev.todoapp.core.database.entity.TaskEntity
import com.techun.dev.todoapp.home.domain.model.Task

fun Task.toEntity() = TaskEntity(
    id = id, title = title, priority = priority, status = status,
    taskDate = date.toEpochDay(),
    startMinute = startTime?.let { it.hour * 60 + it.minute },
    endMinute = endTime?.let { it.hour * 60 + it.minute },
    createdAt = createdAt, updatedAt = updatedAt, completedAt = completedAt
)