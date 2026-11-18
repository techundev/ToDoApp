package com.techun.dev.todoapp.home.ui.composable

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.home.domain.model.RawItem
import com.techun.dev.todoapp.home.domain.model.Task
import com.techun.dev.todoapp.home.domain.model.TimelineItem
import kotlin.math.ceil


private fun layoutTasks(
    tasks: List<Task>,
    startHour: Int,
    endHour: Int,
    minDurationMin: Int
): List<TimelineItem> {
    val rangeStart = startHour * 60
    val rangeEnd = endHour * 60

    val raw = tasks.mapNotNull { task ->
        val s = task.startTime ?: return@mapNotNull null
        val e = task.endTime ?: return@mapNotNull null
        val sm = s.toSecondOfDay() / 60
        val em = e.toSecondOfDay() / 60
        if (em <= sm) return@mapNotNull null

        val vs = maxOf(sm, rangeStart)
        val ve = minOf(em, rangeEnd)
        if (ve <= vs) return@mapNotNull null

        // fin "visual": una tarea corta ocupa al menos la altura mínima de la tarjeta
        RawItem(task, vs, ve, maxOf(ve, vs + minDurationMin))
    }.sortedWith(compareBy<RawItem> { it.startMin }.thenByDescending { it.effectiveEnd })

    val result = mutableListOf<TimelineItem>()

    var cluster = mutableListOf<Pair<RawItem, Int>>()   // item + columna
    val columnEnds = mutableListOf<Int>()               // fin de cada columna
    var clusterEnd = Int.MIN_VALUE


    fun flush() {
        val columns = columnEnds.size
        cluster.forEach { (item, col) ->
            result += TimelineItem(item.task, item.startMin, item.endMin, col, columns)
        }
        cluster = mutableListOf()
        columnEnds.clear()
        clusterEnd = Int.MIN_VALUE
    }

    for (item in raw) {
        if (cluster.isNotEmpty() && item.startMin >= clusterEnd) flush()

        var col = columnEnds.indexOfFirst { it <= item.startMin }
        if (col == -1) {
            columnEnds += item.effectiveEnd
            col = columnEnds.lastIndex
        } else {
            columnEnds[col] = item.effectiveEnd
        }

        cluster += item to col
        clusterEnd = maxOf(clusterEnd, item.effectiveEnd)
    }

    if (cluster.isNotEmpty()) flush()

    return result
}

// ---------- Color estable por tarea ----------

private fun Task.timelineColors(): Pair<Color, Color> {
    val hue = ((id.hashCode() and Int.MAX_VALUE) * 137.508f) % 360f
    val container = Color.hsv(hue, 0.30f, 0.98f)
    val content = Color(0xFF1F1F1F)
    return container to content
}

// ---------- UI ----------

private val MinCardHeight = 44.dp
private val CardStartPadding = 10.dp
private val CardEndPadding = 20.dp
private val CardVerticalPadding = 5.dp
private val ColumnGap = 4.dp

@Composable
fun ToDoDayTimeline(
    modifier: Modifier = Modifier,
    tasks: List<Task>? = emptyList(),
    startHour: Int = 0,
    endHour: Int = 24,
    hourHeight: Dp = 72.dp,
    onTaskClick: (Task) -> Unit = {},
    onTaskLongClick: (Task) -> Unit = {}
) {
    val totalHours = endHour - startHour
    val timelineHeight = hourHeight * totalHours
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    val timelineItems = remember(tasks, startHour, endHour, hourHeight) {
        val minDurationMin = ceil(MinCardHeight / hourHeight * 60f).toInt()
        layoutTasks(tasks.orEmpty(), startHour, endHour, minDurationMin)
    }

    LaunchedEffect(timelineItems, startHour, hourHeight) {
        val first = timelineItems.minByOrNull { it.startMin } ?: return@LaunchedEffect
        val topOffset = hourHeight * ((first.startMin - startHour * 60) / 60f)
        listState.animateScrollToItem(
            index = 0,
            scrollOffset = with(density) { topOffset.roundToPx() }
        )
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(timelineHeight)
            ) {

                // Columna de horas
                Box(
                    modifier = Modifier
                        .weight(0.15f)
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
                            modifier = Modifier.offset(y = hourHeight * hour),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .weight(0.85f)
                        .height(timelineHeight)
                ) {
                    val availableWidth = maxWidth - CardStartPadding - CardEndPadding

                    for (hour in 0..totalHours) {
                        HorizontalDivider(
                            modifier = Modifier.offset(y = hourHeight * hour),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    timelineItems.forEach { item ->
                        val topOffset = hourHeight * ((item.startMin - startHour * 60) / 60f)
                        val duration = item.endMin - item.startMin
                        val cardHeight =
                            (hourHeight * (duration / 60f)).coerceAtLeast(MinCardHeight)

                        val totalGaps = ColumnGap * (item.columns - 1)
                        val columnWidth = (availableWidth - totalGaps) / item.columns
                        val xOffset = CardStartPadding + (columnWidth + ColumnGap) * item.column

                        val (container, content) = remember(item.task.id) {
                            item.task.timelineColors()
                        }

                        Box(
                            modifier = Modifier
                                .offset(x = xOffset, y = topOffset)
                                .width(columnWidth)
                                .height(cardHeight)
                                .padding(vertical = CardVerticalPadding)
                        ) {
                            TaskTimelineCard(
                                task = item.task,
                                containerColor = container,
                                contentColor = content,
                                compact = item.columns > 1,
                                onClick = { onTaskClick(item.task) },
                                onLongClick = { onTaskLongClick(item.task) },
                                modifier = Modifier.fillMaxSize()
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
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    val shape = RoundedCornerShape(if (compact) 16.dp else 28.dp)

    Surface(
        modifier = modifier
            .clip(shape)
            .combinedClickable(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                },
                onLongClickLabel = "Eliminar tarea"
            ),
        shape = shape,
        color = containerColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    horizontal = if (compact) 10.dp else 24.dp,
                    vertical = if (compact) 8.dp else 18.dp
                )
        ) {
            // Título de la task
            ToDoText(
                text = task.title,
                modifier = Modifier.fillMaxWidth(),
                style = (if (compact) MaterialTheme.typography.labelLarge
                else MaterialTheme.typography.titleSmall).copy(fontWeight = FontWeight.SemiBold),
                color = contentColor,
                maxLines = if (compact) 3 else 2
            )

            // Empuja el divisor y el footer hacia el bottom
            Spacer(modifier = Modifier.weight(1f))

            if (!compact) {
                HorizontalDivider(
                    color = contentColor.copy(alpha = 0.10f),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Footer de la task
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(if (compact) 14.dp else 19.dp)
                )
                ToDoText(
                    text = "${task.startTime} - ${task.endTime}",
                    style = if (compact) MaterialTheme.typography.labelSmall
                    else MaterialTheme.typography.bodyMedium,
                    color = contentColor,
                    maxLines = 1
                )
            }
        }
    }
}
