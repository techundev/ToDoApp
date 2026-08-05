package com.techun.dev.todoapp.create.di

import com.techun.dev.todoapp.create.data.repository.AddTaskRepositoryImpl
import com.techun.dev.todoapp.create.domain.repository.CreateTaskRepository
import com.techun.dev.todoapp.create.domain.usecase.AddTaskUseCase
import com.techun.dev.todoapp.create.ui.CreateTaskViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val addTaskModule = module {
    singleOf(::AddTaskRepositoryImpl) { bind<CreateTaskRepository>() }
    factoryOf(::AddTaskUseCase)
    factoryOf(::CreateTaskViewModel)
}