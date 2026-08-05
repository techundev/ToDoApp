package com.techun.dev.todoapp.home.domain.model

sealed class HomeResult {
    data class Success(val tasks: List<Task>) : HomeResult()
    data object Empty : HomeResult()
    data class Error(val message: String) : HomeResult()
}