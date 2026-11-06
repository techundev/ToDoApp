package com.techun.dev.todoapp.create.domain.model

import com.techun.dev.todoapp.home.domain.model.PriorityStatus
import com.techun.dev.todoapp.home.domain.model.TaskStatus
import java.time.LocalDate
import java.time.LocalTime

data class NewTask(
    val title: String,
    val priority: PriorityStatus,
    val status: TaskStatus,
    val date: LocalDate,
    val startTime: LocalTime?,
    val endTime: LocalTime?
)
