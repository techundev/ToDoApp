package com.techun.dev.todoapp.home.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techun.dev.todoapp.core.composables.ToDoText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ToDoHorizontalCalendar(
    selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit, modifier: Modifier = Modifier
) {
    val today = LocalDate.now()

    val days = remember(today) {
        val firstDay = today.withDayOfMonth(1)
        val lastDay = today.withDayOfMonth(today.lengthOfMonth())

        (1..lastDay.dayOfMonth).map { day ->
            firstDay.withDayOfMonth(day)
        }
    }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = today.dayOfMonth - 1
    )

    Column {
        Spacer(modifier = Modifier.height(10.dp))
        ToDoText(
            text = selectedDate.format(
                DateTimeFormatter.ofPattern(
                    "MMMM d", Locale("es", "GT")
                )
            ).replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(
                start = 12.dp, bottom = 12.dp
            )
        )

        LazyRow(
            state = listState,
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = days, key = { it.toString() }) { date ->

                CalendarDayItem(
                    date = date, selected = date == selectedDate, onClick = {
                        onDateSelected(date)
                    })
            }
        }
    }
}


@Composable
fun CalendarDayItem(
    date: LocalDate, selected: Boolean, onClick: () -> Unit
) {
    val background = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = Modifier
            .width(70.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ToDoText(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = contentColor
        )
        ToDoText(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = date.dayOfWeek.getDisplayName(
                TextStyle.SHORT, Locale("es", "GT")
            ).uppercase().take(3),
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
    }
}