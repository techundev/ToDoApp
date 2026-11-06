package com.techun.dev.todoapp.home.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Task(
    val id: Long,
    val title: String,
    val priority: PriorityStatus,
    val status: TaskStatus,
    val date: LocalDate,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val createdAt: Long,
    val updatedAt: Long?,
    val completedAt: Long?
) {
    val isCompleted: Boolean get() = status == TaskStatus.COMPLETED
}