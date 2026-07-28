package com.techun.dev.todoapp.home.data.utils

import com.techun.dev.todoapp.home.data.local.dao.TaskDao
import com.techun.dev.todoapp.home.data.local.entity.TaskEntity
import com.techun.dev.todoapp.home.domain.model.PriorityStatus
import com.techun.dev.todoapp.home.domain.model.TaskStatus

object TaskSeeder {
    suspend fun seedIfEmpty(dao: TaskDao) {
        if (dao.count() > 0) return

        val now = System.currentTimeMillis()

        val tasks = listOf(
            TaskEntity(
                title = "Instalar POS Retail",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Actualizar Menú Digital",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Instalar Facturación",
                priority = PriorityStatus.LOW,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Instalar Inventario",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Actualizar Terminal Bank",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            )
        )

        dao.insertAll(tasks)
    }
}