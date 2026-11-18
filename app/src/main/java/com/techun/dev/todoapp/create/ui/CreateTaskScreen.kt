package com.techun.dev.todoapp.create.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techun.dev.todoapp.core.composables.ToDoText
import com.techun.dev.todoapp.create.ui.composable.TodoDiaSelectionCard
import com.techun.dev.todoapp.create.ui.composable.TodoDiaTimePickerDialog
import com.techun.dev.todoapp.create.ui.composable.TodoDiaTimeSelector
import org.koin.compose.viewmodel.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CreateTaskScreen(
    viewModel: CreateTaskViewModel = koinViewModel(), onBackToHome: () -> Unit
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
    CreateTaskContent(snackbarHostState = snackbarHostState)
}


@Composable
fun CreateTaskContent(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSaveClick: (
        description: String, date: LocalDate, startTime: LocalTime, endTime: LocalTime, reminder: Boolean, type: String
    ) -> Unit = { _, _, _, _, _, _ -> }
) {
    var description by remember {
        mutableStateOf("Meeting with PR Department")
    }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var startTime by remember {
        mutableStateOf(LocalTime.of(8, 0))
    }

    var endTime by remember {
        mutableStateOf(LocalTime.of(10, 0))
    }

    var reminder by remember {
        mutableStateOf(false)
    }

    var selectedType by remember {
        mutableStateOf("Trabajo")
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var showStartTimePicker by remember {
        mutableStateOf(false)
    }

    var showEndTimePicker by remember {
        mutableStateOf(false)
    }

    var showTypeMenu by remember {
        mutableStateOf(false)
    }

    val types = listOf(
        "Trabajo", "Personal", "Estudio", "Salud", "Otros"
    )

    val dateFormatter = remember {
        DateTimeFormatter.ofPattern(
            "EEEE, d 'de' MMMM", Locale("es", "GT")
        )
    }

    val timeFormatter = remember {
        DateTimeFormatter.ofPattern(
            "h a", Locale.ENGLISH
        )
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                ToDoText(
                    text = "Editar tarea",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 58.dp)
                        .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDDC9F5))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    BasicTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(
                            fontSize = 16.sp, color = Color(0xFF252525)
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (description.isEmpty()) {
                                    ToDoText(
                                        "Descripción de la tarea",
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        })

                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Editar descripción",
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                HorizontalDivider(
                    color = Color(0xFFE6E3E0), modifier = Modifier.padding(vertical = 12.dp)
                )

                // Fecha
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable {
                            showDatePicker = true
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFEEE8)), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFFFF7555)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    ToDoText(
                        text = selectedDate.format(dateFormatter)
                            .replaceFirstChar { it.uppercase() },
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )

                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                HorizontalDivider(color = Color(0xFFE6E3E0))

                // Horario
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TodoDiaTimeSelector(
                        time = startTime.format(timeFormatter), onClick = {
                            showStartTimePicker = true
                        }, modifier = Modifier.weight(1f)
                    )

                    ToDoText(
                        text = "-",
                        fontSize = 20.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )

                    TodoDiaTimeSelector(
                        time = endTime.format(timeFormatter), onClick = {
                            showEndTimePicker = true
                        }, modifier = Modifier.weight(1f)
                    )
                }

                HorizontalDivider(color = Color(0xFFE6E3E0))

                Spacer(modifier = Modifier.height(22.dp))

                // Tipo de tarea
                Box {
                    TodoDiaSelectionCard(
                        icon = Icons.Rounded.WorkOutline,
                        iconBackground = Color(0xFFF3EAFE),
                        iconTint = Color(0xFFAE7BE0),
                        title = selectedType,
                        trailing = {
                            Icon(
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            showTypeMenu = true
                        })

                    DropdownMenu(
                        expanded = showTypeMenu, onDismissRequest = {
                            showTypeMenu = false
                        }) {
                        types.forEach { type ->
                            DropdownMenuItem(text = {
                                ToDoText(type)
                            }, onClick = {
                                selectedType = type
                                showTypeMenu = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Recordatorio
                TodoDiaSelectionCard(
                    icon = Icons.Rounded.NotificationsNone,
                    iconBackground = Color(0xFFEAF7FA),
                    iconTint = Color(0xFF55BCCC),
                    title = "Recordatorio",
                    trailing = {
                        Switch(
                            checked = reminder, onCheckedChange = {
                                reminder = it
                            })
                    },
                    onClick = {
                        reminder = !reminder
                    })
            }

            // Botón guardar
            Button(
                onClick = {
                    onSaveClick(
                        description, selectedDate, startTime, endTime, reminder, selectedType
                    )
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp, vertical = 16.dp
                    )
                    .height(54.dp), shape = RoundedCornerShape(16.dp)
            ) {
                ToDoText(
                    text = "Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Date Picker
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()
            )

            DatePickerDialog(onDismissRequest = {
                showDatePicker = false
            }, confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate =
                                Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                        }
                        showDatePicker = false
                    }) {
                    ToDoText("Aceptar")
                }
            }, dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }) {
                    ToDoText("Cancelar")
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }

        // Start Time Picker
        if (showStartTimePicker) {
            TodoDiaTimePickerDialog(initialTime = startTime, onDismiss = {
                showStartTimePicker = false
            }, onConfirm = {
                startTime = it
                showStartTimePicker = false
            })
        }

        // End Time Picker
        if (showEndTimePicker) {
            TodoDiaTimePickerDialog(initialTime = endTime, onDismiss = {
                showEndTimePicker = false
            }, onConfirm = {
                endTime = it
                showEndTimePicker = false
            })
        }
    }
}