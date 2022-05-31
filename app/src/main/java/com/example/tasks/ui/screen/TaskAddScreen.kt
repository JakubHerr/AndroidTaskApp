package com.example.tasks.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tasks.R
import com.example.tasks.data.Task
import com.example.tasks.ui.DeadlineButton
import com.example.tasks.ui.EstimatePicker
import com.example.tasks.ui.PriorityDropdownMenu
import com.example.tasks.ui.TaskNameField

enum class TaskPriority {
    NO, LOW, MEDIUM, HIGH
}

sealed class TaskAddScreenEvent {
    data class AddTask(val task: Task) : TaskAddScreenEvent()
    object Cancel : TaskAddScreenEvent()
    data class SetDeadline(val task: Task) : TaskAddScreenEvent()
}

@Composable
fun TaskAddScreen(onEvent: (TaskAddScreenEvent) -> Unit) {

    var showDropdownMenu by remember { mutableStateOf(false) }
    var showEstimatePicker by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    val task by remember { mutableStateOf(Task()) }

    Scaffold(floatingActionButton = {
        SaveTaskFab {
            task.taskName = name.ifBlank { "Untitled" }
            onEvent(TaskAddScreenEvent.AddTask(task))
        }
    }, floatingActionButtonPosition = FabPosition.End) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskNameField(name) { input -> name = input }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //deadline button
                DeadlineButton(task, onEvent)

                //cancel button
                Button(onClick = {
                    onEvent(TaskAddScreenEvent.Cancel)
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
                Button(onClick = { showEstimatePicker = true }) {
                    Text("Time estimate")
                }
                if (showEstimatePicker) {
                    EstimatePicker(
                        onCancel = {
                            task.timeEstimate = 0
                            showEstimatePicker = false
                        },
                        onDismiss = { showEstimatePicker = false }, onSet = { minutes ->
                            task.timeEstimate = minutes
                            showEstimatePicker = false
                        })
                }
            }
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
