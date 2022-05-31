package com.example.tasks.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tasks.R
import com.example.tasks.data.Task
import kotlinx.datetime.*

enum class TaskPriority {
    NO, LOW, MEDIUM, HIGH
}

@Composable
fun TaskAddScreen(onTaskAdd: (Task) -> Unit, onCancel: () -> Unit) {

    var showDropdownMenu by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    val task by remember { mutableStateOf(Task()) }

    Scaffold(floatingActionButton = {
        SaveTaskFab {
            task.taskName = name.ifBlank { "Untitled" }
            onTaskAdd(task)
        }
    }, floatingActionButtonPosition = FabPosition.End) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.task_name_prompt)) })
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //deadline button
                DeadlineButton(task)

                //cancel button
                Button(onClick = {
                    onCancel()
                }) {
                    Text(text = stringResource(id = R.string.cancel_button))
                }
            }
            Row(Modifier.fillMaxWidth()) {
                //priority button
                Button(onClick = { showDropdownMenu = !showDropdownMenu }) {
                    Text(text = stringResource(id = R.string.priority))
                    PriorityDropdownMenu(expanded = showDropdownMenu) { priority ->
                        task.priority = priority.ordinal.toByte()
                        showDropdownMenu = false
                    }
                }
            }
        }
    }
}

//TODO refactor with Material2 components
@Composable
private fun DeadlineButton(task: Task) {
    val context = LocalContext.current
    val offsetTimeZone = TimeZone.currentSystemDefault()

    Button(onClick = {
        Log.d("DatePicker", "Date Picker button pressed")
        val dialog = DatePickerDialog(context)

        dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            Log.d("DatePicker", "date set to $dayOfMonth/$month/$year")
            val listener = OnTimeSetListener { _, hourOfDay, minute ->
                Log.d("TimePicker", "Time set to $hourOfDay:$minute")
                //zero indexed month meets one indexed month
                task.deadline = LocalDateTime(year, month+1, dayOfMonth, hourOfDay, minute)
                task.timezone = offsetTimeZone
            }
            TimePickerDialog(context, listener, 12, 0, true).show()
        }
        dialog.setOnDismissListener {
            task.deadline = null
        }

        dialog.show()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
            contentDescription = stringResource(
                id = R.string.time_picker_description
            )
        )//TODO format date
        Text(task.deadline?.toString() ?: stringResource(id = R.string.no_date_selected))
    }
}

@Composable
private fun PriorityDropdownMenu(expanded: Boolean, onSelect: (TaskPriority) -> Unit) {
    DropdownMenu(expanded = expanded, onDismissRequest = { onSelect(TaskPriority.NO) }) {
        DropdownMenuItem(onClick = { onSelect(TaskPriority.HIGH) }) {
            Text("High")
        }
        DropdownMenuItem(onClick = { onSelect(TaskPriority.MEDIUM) }) {
            Text("Medium")
        }
        DropdownMenuItem(onClick = { onSelect(TaskPriority.LOW) }) {
            Text("Low")
        }
        DropdownMenuItem(onClick = { onSelect(TaskPriority.NO) }) {
            Text("No")
        }
    }
}

@Composable
private fun SaveTaskFab(onClick: () -> Unit) {
    FloatingActionButton(onClick = { onClick() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_save_24),
            contentDescription = "Save task"
        )
    }
}