package com.techun.dev.todoapp.core.nav3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavRoutes : NavKey {
    @Serializable
    data object Home : NavRoutes


    @Serializable
    data object Add : NavRoutes
}