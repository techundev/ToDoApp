package com.techun.dev.todoapp.home.di

import androidx.room.Room
import com.techun.dev.todoapp.home.data.local.database.TaskDatabase
import com.techun.dev.todoapp.home.data.repository.TaskRepositoryImpl
import com.techun.dev.todoapp.home.domain.repository.HomeRepository
import com.techun.dev.todoapp.home.domain.usecase.GetCompletedTasksUseCase
import com.techun.dev.todoapp.home.domain.usecase.GetPendingTasksUseCase
import com.techun.dev.todoapp.home.domain.usecase.ToggleTaskCompletedUseCase
import com.techun.dev.todoapp.home.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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


val homeModule = module {
    singleOf(::TaskRepositoryImpl) { bind<HomeRepository>() }
    factoryOf(::GetPendingTasksUseCase)
    factoryOf(::GetCompletedTasksUseCase)
    factoryOf(::ToggleTaskCompletedUseCase)
    viewModelOf(::HomeViewModel)
}