package com.techun.dev.todoapp.core.composables

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.techun.dev.todoapp.core.utils.AppIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDiaTopAppBar(
    title: String, onBack: () -> Unit, modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(modifier = modifier, navigationIcon = {
        IconButton(
            onClick = onBack,
        ) {
            Icon(imageVector = AppIcons.arrow_back, contentDescription = null)
        }
    }, actions = {}, title = {
        ToDoText(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    })
}