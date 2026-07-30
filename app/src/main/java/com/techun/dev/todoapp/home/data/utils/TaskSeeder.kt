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
            // -------------------- PENDING --------------------

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
                title = "Instalar Facturación Electrónica",
                priority = PriorityStatus.LOW,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Configurar impresora térmica",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Revisar conexión VPN",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Actualizar catálogo de productos",
                priority = PriorityStatus.LOW,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Crear respaldo de base de datos",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Sincronizar inventario",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Agregar nuevo usuario",
                priority = PriorityStatus.LOW,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Configurar método de pago",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Actualizar certificado SSL",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Optimizar rendimiento de la app",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Revisar permisos de usuarios",
                priority = PriorityStatus.LOW,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Migrar configuración del servidor",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),
            TaskEntity(
                title = "Enviar reporte semanal",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.PENDING,
                createdAt = now,
                updatedAt = now,
                completedAt = null,
                deletedAt = null
            ),

            // -------------------- COMPLETED --------------------

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
            ),
            TaskEntity(
                title = "Configurar lector QR",
                priority = PriorityStatus.LOW,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Registrar nueva sucursal",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Actualizar firmware POS",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Validar token de acceso",
                priority = PriorityStatus.LOW,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Crear respaldo automático",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Corregir error de sincronización",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Actualizar API de pagos",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Optimizar consultas Room",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Limpiar caché de imágenes",
                priority = PriorityStatus.LOW,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Actualizar dependencias Gradle",
                priority = PriorityStatus.HIGH,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Implementar modo oscuro",
                priority = PriorityStatus.MEDIUM,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Agregar validación de formularios",
                priority = PriorityStatus.LOW,
                status = TaskStatus.COMPLETED,
                createdAt = now,
                updatedAt = now,
                completedAt = now,
                deletedAt = null,
                isCompleted = true
            ),
            TaskEntity(
                title = "Publicar versión 1.0.0",
                priority = PriorityStatus.HIGH,
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