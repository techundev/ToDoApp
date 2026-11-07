package com.techun.dev.todoapp.home.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.home.domain.model.Task

@Composable
fun ToDoDayTimeline(
    tasks: List<Task>,
    modifier: Modifier = Modifier,
    startHour: Int = 0,
    endHour: Int = 24,
    hourHeight: Dp = 72.dp
) {
    val totalHours = endHour - startHour
    val timelineHeight = hourHeight * totalHours

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(timelineHeight),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                // Columna de horas
                Box(
                    modifier = Modifier
                        .width(58.dp)
                        .padding(start = 16.dp)
                        .fillMaxHeight()
                ) {
                    for (hour in 0 until totalHours) {
                        ToDoText(
                            text = when (val h = startHour + hour) {
                                0 -> "12 AM"
                                in 1..11 -> "$h AM"
                                12 -> "12 PM"
                                else -> "${h - 12} PM"
                            },
                            modifier = Modifier.offset(
                                y = hourHeight * hour
                            ),

                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Área de tareas
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(timelineHeight)
                ) {

                    // Líneas horizontales de cada hora
                    for (hour in 0..totalHours) {
                        HorizontalDivider(
                            modifier = Modifier.offset(
                                y = hourHeight * hour
                            ), color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    // Tareas posicionadas según su horario
                    tasks.forEach { task ->

                        val start = task.startTime ?: return@forEach
                        val end = task.endTime ?: return@forEach

                        val startMinutes = start.toSecondOfDay() / 60
                        val endMinutes = end.toSecondOfDay() / 60

                        if (endMinutes <= startMinutes) return@forEach

                        // Limitar las tareas al rango visible
                        val visibleStart = maxOf(
                            startMinutes, startHour * 60
                        )

                        val visibleEnd = minOf(
                            endMinutes, endHour * 60
                        )

                        if (visibleEnd <= visibleStart) return@forEach

                        val topOffset = hourHeight * ((visibleStart - startHour * 60) / 60f)

                        val duration = visibleEnd - visibleStart

                        val cardHeight = (hourHeight * (duration / 60f)).coerceAtLeast(44.dp)

                        Box(
                            modifier = Modifier
                                .offset(y = topOffset)
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 20.dp, top = 5.dp)
                                .height(cardHeight)
                        ) {
                            TaskTimelineCard(
                                task = task, modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskTimelineCard(
    task: Task,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 24.dp, vertical = 18.dp)
        ) {
            // Título de la task
            ToDoText(
                text = task.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 2
            )

            // Empuja el divisor y el footer hacia el bottom
            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.10f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer de la task
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(19.dp)
                )

                ToDoText(
                    text = "${task.startTime} - ${task.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}