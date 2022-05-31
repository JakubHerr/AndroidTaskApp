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
import kotlinx.coroutines.flow.Flow

sealed class TaskEditScreenEvent {
    data class EditTask(val task: Task): TaskEditScreenEvent()
    data class DeleteTask(val task: Task): TaskEditScreenEvent()
}

@Composable
fun TaskEditScreen(retrievedTask: Flow<Task>, onEvent: (TaskEditScreenEvent) -> Unit) {
    val test = retrievedTask.collectAsState(initial = null)
    var showDropdownMenu by remember { mutableStateOf(false) }
    var showEstimatePicker by remember { mutableStateOf(false) }

    //makes sure newName is first set after task has been already retrieved
    test.value?.let {
        val newTask by remember{ mutableStateOf(it.copy()) }

        Scaffold(floatingActionButton = {
            EditTaskFab {
                newTask.taskName = newTask.taskName.ifBlank { "Untitled" }
                onEvent(TaskEditScreenEvent.EditTask(it))
            }
        }, floatingActionButtonPosition = FabPosition.End) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TaskNameField(newTask.taskName) { input -> newTask.taskName = input }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //deadline button
                    //TODO create a sealed class for events shared between Add and Edit Task
                    //DeadlineButton(newTask, onEvent)

                    //delete button
                    Button(onClick = {
                        onEvent(TaskEditScreenEvent.DeleteTask(newTask))
                    }) {
                        Text(text = stringResource(id = R.string.delete_button))
                    }
                }
                Row(Modifier.fillMaxWidth()) {
                    //priority button
                    Button(onClick = { showDropdownMenu = !showDropdownMenu }) {
                        Text(text = stringResource(id = R.string.priority))
                        PriorityDropdownMenu(expanded = showDropdownMenu) { priority ->
                            newTask.priority = priority.ordinal.toByte()
                            showDropdownMenu = false
                        }
                    }
                    Button(onClick = { showEstimatePicker = true }) {
                        Text("Time estimate")
                    }
                    if (showEstimatePicker) {
                        EstimatePicker(
                            onCancel = {
                                newTask.timeEstimate = 0
                                showEstimatePicker = false
                            },
                            onDismiss = { showEstimatePicker = false }, onSet = { minutes ->
                                newTask.timeEstimate = minutes
                                showEstimatePicker = false
                            })
                    }
                }
            }
        }
    }
}

@Composable
private fun EditTaskFab(onClick: () -> Unit) {
    FloatingActionButton(onClick = { onClick() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_edit_24),
            contentDescription = "Edit task"
        )
    }
}