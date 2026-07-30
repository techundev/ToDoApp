package com.techun.dev.todoapp.core.database.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techun.dev.todoapp.core.database.local.dao.TaskDao
import com.techun.dev.todoapp.core.database.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}