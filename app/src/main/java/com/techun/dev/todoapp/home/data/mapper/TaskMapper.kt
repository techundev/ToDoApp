package com.techun.dev.todoapp.home.data.mapper

import com.techun.dev.todoapp.core.database.entity.TaskEntity
import com.techun.dev.todoapp.home.domain.model.Task
import java.time.LocalDate
import java.time.LocalTime

fun TaskEntity.toDomain() = Task(
    id = id, title = title, priority = priority, status = status,
    date = LocalDate.ofEpochDay(taskDate),
    startTime = startMinute?.let { LocalTime.of(it / 60, it % 60) },
    endTime = endMinute?.let { LocalTime.of(it / 60, it % 60) },
    createdAt = createdAt, updatedAt = updatedAt, completedAt = completedAt
)