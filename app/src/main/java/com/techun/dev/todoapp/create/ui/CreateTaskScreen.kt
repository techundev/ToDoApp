package com.techun.dev.todoapp.create.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.core.utils.color
import com.techun.dev.todoapp.create.ui.composable.ToDoButton
import com.techun.dev.todoapp.create.ui.composable.ToDoTextField
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateTaskScreen(
    viewModel: CreateTaskViewModel = koinViewModel(),
    onBackToHome: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CreateTaskEvent.TaskCreated -> onBackToHome()
                is CreateTaskEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    CreateTaskScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onTitleChanged = viewModel::onTitleChanged,
        onPriorityChanged = viewModel::onPriorityChanged,
        onDoneClicked = viewModel::onDoneClicked
    )
}

@Composable
fun CreateTaskScreen(
    uiState: CreateTaskUiState,
    snackbarHostState: SnackbarHostState,
    onTitleChanged: (String) -> Unit,
    onPriorityChanged: (Float) -> Unit,
    onDoneClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(35.dp))

            ToDoText(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.displayLarge,
                text = "Create Task",
            )

            Spacer(modifier = Modifier.height(35.dp))

            ToDoTextField(
                value = uiState.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onValueChange = onTitleChanged,
                label = "Task Name"
            )

            Spacer(modifier = Modifier.height(35.dp))


            PrioritySlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                priority = uiState.priority,
                onPriorityChanged = onPriorityChanged
            )

            Spacer(modifier = Modifier.weight(1f))

            ToDoButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Done",
                onclick = onDoneClicked
            )

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySlider(
    priority: Float,
    modifier: Modifier = Modifier,
    onPriorityChanged: (Float) -> Unit
) {
    val animatedPriority by animateFloatAsState(
        targetValue = priority,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "priorityPosition"
    )

    val animatedColor by animateColorAsState(
        targetValue = priority.color(),
        animationSpec = tween(durationMillis = 250),
        label = "priorityColor"
    )

    Column(
        modifier = modifier
    ) {
        ToDoText(text = "Priority")

        Slider(
            value = animatedPriority,
            onValueChange = onPriorityChanged,
            valueRange = 0f..2f,
            steps = 1,
            colors = SliderDefaults.colors(
                thumbColor = animatedColor,
                activeTrackColor = animatedColor,
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .border(2.dp, animatedColor, CircleShape)
                        .background(Color.White, CircleShape)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(2.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = animatedColor,
                        inactiveTrackColor = Color.LightGray
                    ),
                    thumbTrackGapSize = 0.dp,
                    drawTick = { offset, color ->
                        drawCircle(
                            color = Color.White,
                            radius = 5.dp.toPx(),
                            center = offset
                        )
                        drawCircle(
                            color = color,
                            radius = 5.dp.toPx(),
                            center = offset,
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    },
                    drawStopIndicator = { offset ->
                        drawCircle(
                            color = Color.White,
                            radius = 5.dp.toPx(),
                            center = offset
                        )
                        drawCircle(
                            color = animatedColor,
                            radius = 5.dp.toPx(),
                            center = offset,
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                )
            })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ToDoText("Low")
            ToDoText("Medium")
            ToDoText("High")
        }
    }
}