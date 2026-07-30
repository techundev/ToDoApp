package com.techun.dev.todoapp.create.domain.model

sealed class CreateTaskResult {
    data object Success : CreateTaskResult()
    data object Empty : CreateTaskResult()
    data class Error(val message: String) : CreateTaskResult()
}