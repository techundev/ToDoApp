package com.techun.dev.todoapp.create.data.mapper

import com.techun.dev.todoapp.core.database.entity.TaskEntity
import com.techun.dev.todoapp.create.domain.model.NewTask
import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.domain.model.TaskStatus
import java.time.LocalTime

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    priority = priority,
    status = status,
    taskDate = date.toEpochDay(),
    startMinute = startTime?.let { it.hour * 60 + it.minute },
    endMinute = endTime?.let { it.hour * 60 + it.minute },
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt
)

fun NewTask.toEntity(now: Long): TaskEntity {
    return TaskEntity(
        title = title.trim(),
        priority = priority,
        status = status,
        taskDate = date.toEpochDay(),
        startMinute = startTime?.toMinuteOfDay(),
        endMinute = endTime?.toMinuteOfDay(),
        createdAt = now,
        completedAt = if (status == TaskStatus.COMPLETED) now else null
    )
}

private fun LocalTime.toMinuteOfDay(): Int = hour * 60 + minute