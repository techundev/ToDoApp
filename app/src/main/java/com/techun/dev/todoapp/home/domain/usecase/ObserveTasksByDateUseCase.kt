package com.techun.dev.todoapp.home.domain.usecase

import com.techun.dev.todoapp.home.domain.repository.HomeRepository
import java.time.LocalDate

class ObserveTasksByDateUseCase(private val repository: HomeRepository) {
    operator fun invoke(day: LocalDate) = repository.observeTasksByDate(day)
}