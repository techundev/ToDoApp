package com.techun.dev.todoapp.home.domain.repository

import com.techun.dev.todoapp.home.domain.model.HomeResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getPendingTask(): Flow<HomeResult>
    fun getCompletedTask(): Flow<HomeResult>
}