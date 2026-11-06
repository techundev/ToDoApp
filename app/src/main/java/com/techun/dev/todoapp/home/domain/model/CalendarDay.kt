package com.techun.dev.todoapp.home.domain.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val dayName: String,
    val dayNumber: String,
    val isToday: Boolean
)
