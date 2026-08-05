package com.techun.dev.todoapp.home.di

import com.techun.dev.todoapp.home.data.repository.TaskRepositoryImpl
import com.techun.dev.todoapp.home.domain.repository.HomeRepository
import com.techun.dev.todoapp.home.domain.usecase.DeleteTaskByIdUseCase
import com.techun.dev.todoapp.home.domain.usecase.GetCompletedTasksUseCase
import com.techun.dev.todoapp.home.domain.usecase.GetPendingTasksUseCase
import com.techun.dev.todoapp.home.domain.usecase.ToggleTaskCompletedUseCase
import com.techun.dev.todoapp.home.ui.HomeViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    singleOf(::TaskRepositoryImpl) { bind<HomeRepository>() }
    factoryOf(::GetPendingTasksUseCase)
    factoryOf(::GetCompletedTasksUseCase)
    factoryOf(::ToggleTaskCompletedUseCase)
    factoryOf(::DeleteTaskByIdUseCase)
    viewModelOf(::HomeViewModel)
}