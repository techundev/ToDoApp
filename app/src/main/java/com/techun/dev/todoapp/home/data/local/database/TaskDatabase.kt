package com.techun.dev.todoapp.home.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techun.dev.todoapp.home.data.local.dao.TaskDao
import com.techun.dev.todoapp.home.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}