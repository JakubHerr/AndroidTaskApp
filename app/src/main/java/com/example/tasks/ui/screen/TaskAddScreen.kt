package com.example.tasks.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.tasks.R
import com.example.tasks.data.Task
import java.util.*

@Composable
fun TaskAddScreen(onTaskAdd: (Task) -> Unit, onCancel: () -> Unit) {

    var name by remember {
        mutableStateOf("")
    }

    val task by remember {
        mutableStateOf(Task())
    }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(id = R.string.task_name_prompt)) })
        //add button
        Button(onClick = {
            task.taskName = name.ifBlank { "Untitled" }
            onTaskAdd(task)
        }) {
            Text(text = stringResource(id = R.string.add_task))
        }

        //cancel button
        Button(onClick = {
            onCancel()
        }) {
            Text(text = stringResource(id = R.string.cancel_button))
        }
        //priority button
        Button(onClick = {
            //TODO add priority dropdown menu
        }) {
            Text(text = stringResource(id = R.string.priority))
        }

        //deadline button
        Button(onClick = {
            Log.d("DatePicker", "Date Picker button pressed")
            val dialog = DatePickerDialog(context)

            dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                Log.d("DatePicker", "date set to $dayOfMonth/$month/$year")
                val listener = OnTimeSetListener { _, hourOfDay, minute ->
                    Log.d("TimePicker", "Time set to $hourOfDay:$minute")
                    val deadline = Calendar
                        .Builder()
                        .setDate(year, month, dayOfMonth)
                        .setTimeOfDay(hourOfDay, minute, 0)
                        .build()
                    task.deadline = deadline
                }
                TimePickerDialog(context, listener, 12, 0, true).show()
            }
            dialog.setOnDismissListener {
                task.deadline = Calendar.Builder().setDate(1970, 1, 1).setTimeOfDay(0, 0, 0).build()
            }

            dialog.show()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                contentDescription = stringResource(
                    id = R.string.time_picker_description
                )
            )
        }
    }
}