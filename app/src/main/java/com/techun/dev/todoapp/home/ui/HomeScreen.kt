package com.techun.dev.todoapp.home.ui

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.techun.dev.todoapp.R
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.home.domain.model.HomeScreenPhase
import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.ui.composable.ToDoTaskFloatingActionButton
import com.techun.dev.todoapp.home.ui.composable.ToDoTaskItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(), onCreateNewTask: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentPhase = when {
        uiState.pending is TaskSectionState.Loading && uiState.completed is TaskSectionState.Loading ->
            HomeScreenPhase.Loading

        uiState.pending is TaskSectionState.Empty && uiState.completed is TaskSectionState.Empty ->
            HomeScreenPhase.Empty

        else -> HomeScreenPhase.Content
    }

    Scaffold(
        floatingActionButton = {
            ToDoTaskFloatingActionButton { onCreateNewTask() }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Spacer(modifier = Modifier.height(35.dp))

            ToDoText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "ToDo",
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(35.dp))

            AnimatedContent(
                targetState = currentPhase,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(300)) + scaleIn(
                        initialScale = 0.95f,
                        animationSpec = tween(300)
                    ))
                        .togetherWith(fadeOut(animationSpec = tween(150)))
                },
                label = "homeScreenPhaseTransition"
            ) { phase ->
                when (phase) {
                    HomeScreenPhase.Loading -> TaskSectionLoading()
                    HomeScreenPhase.Empty -> TaskSectionEmpty()
                    HomeScreenPhase.Content -> HomeContent(
                        uiState = uiState,
                        onDelete = { task -> viewModel.deleteTask(task) },
                        onComplete = { task -> viewModel.taskToggleCompleted(task) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onDelete: (Task) -> Unit,
    onComplete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        taskSection(
            key = "pending", state = uiState.pending, onDelete = onDelete, onComplete = onComplete
        )

        if (uiState.pending !is TaskSectionState.Empty && uiState.completed !is TaskSectionState.Empty) {

            item(
                key = "divider"
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.LightGray)
                )
            }
        }

        taskSection(
            key = "completed",
            state = uiState.completed,
            onDelete = onDelete,
            onComplete = onComplete
        )
    }
}

private fun LazyListScope.taskSection(
    key: String, state: TaskSectionState, onDelete: (Task) -> Unit, onComplete: (Task) -> Unit
) {
    when (state) {
        is TaskSectionState.Error -> item(key = "${key}_error", contentType = "error") {
            TaskSectionError(message = state.message)
        }

        is TaskSectionState.Success -> items(
            items = state.tasks,
            key = { "${key}_${it.id}" },
            contentType = { "task" }) { task ->
            ToDoTaskItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .animateItem(),
                task = task,
                onDelete = onDelete,
                onComplete = onComplete
            )
        }

        else -> Unit
    }
}

@Composable
fun TaskSectionLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TaskSectionEmpty(
    modifier: Modifier = Modifier,
    @RawRes rawRes: Int = R.raw.embty_cart
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(300.dp)
        )

        ToDoText(
            text = "TODO's you add will appear here",
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
    }
}

@Composable
private fun TaskSectionError(message: String) {
    ToDoText(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
    )
}
