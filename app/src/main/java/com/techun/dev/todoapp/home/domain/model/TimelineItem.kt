package com.techun.dev.todoapp.home.domain.model

data class TimelineItem(
    val task: Task,
    val startMin: Int,
    val endMin: Int,
    val column: Int,
    val columns: Int
)