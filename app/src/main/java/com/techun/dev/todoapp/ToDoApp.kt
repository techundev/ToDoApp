package com.techun.dev.todoapp

import android.app.Application
import com.techun.dev.todoapp.core.di.databaseModule
import com.techun.dev.todoapp.create.di.addTaskModule
import com.techun.dev.todoapp.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class ToDoApp : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ToDoApp)
            modules(
                databaseModule, homeModule, addTaskModule
            )
        }
    }
}