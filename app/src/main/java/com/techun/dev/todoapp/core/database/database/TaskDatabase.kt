package com.techun.dev.todoapp.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techun.dev.todoapp.core.database.dao.TaskDao
import com.techun.dev.todoapp.core.database.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}