package com.techun.dev.todoapp.core.di

import androidx.room.Room
import com.techun.dev.todoapp.core.database.local.database.TaskDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val TASK_DATABASE = "task_database"


val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = TaskDatabase::class.java,
            name = TASK_DATABASE
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single { get<TaskDatabase>().taskDao() }
}
