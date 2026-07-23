package com.techun.dev.todoapp

import android.app.Application
import com.techun.dev.todoapp.home.data.local.dao.TaskDao
import com.techun.dev.todoapp.home.data.utils.TaskSeeder
import com.techun.dev.todoapp.home.di.databaseModule
import com.techun.dev.todoapp.home.di.homeModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class ToDoApp : Application(), KoinComponent {

    private val taskDao: TaskDao by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ToDoApp)
            modules(
                databaseModule, homeModule
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            TaskSeeder.seedIfEmpty(taskDao)
        }
    }
}