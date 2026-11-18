package com.techun.dev.todoapp.create.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.techun.dev.todoapp.core.composables.ToDoText
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDiaTimePickerDialog(
    initialTime: LocalTime, onDismiss: () -> Unit, onConfirm: (LocalTime) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialTime.hour, initialMinute = initialTime.minute, is24Hour = false
    )

    AlertDialog(onDismissRequest = onDismiss, title = {
        ToDoText("Seleccionar hora")
    }, text = {
        TimePicker(state = state)
    }, confirmButton = {
        TextButton(
            onClick = {
                onConfirm(
                    LocalTime.of(
                        state.hour, state.minute
                    )
                )
            }) {
            ToDoText("Aceptar")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            ToDoText("Cancelar")
        }
    })
}