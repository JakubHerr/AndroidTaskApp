package com.example.tasks.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.tasks.R
import com.example.tasks.data.Task
import com.example.tasks.ui.screen.TaskAddScreen
import com.example.tasks.ui.screen.TaskAddScreenEvent
import com.example.tasks.ui.theme.TasksTheme
import com.example.tasks.ui.viewmodel.TaskAddViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.*

@AndroidEntryPoint
class TaskAddFragment : Fragment() {
    private val viewModel: TaskAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            id = R.id.taskAddFragment

            setContent {
                TasksTheme {
                    TaskAddScreen { event ->
                        when (event) {
                            is TaskAddScreenEvent.AddTask -> {
                                viewModel.addTask(event.task)
                                findNavController().navigateUp()
                            }
                            is TaskAddScreenEvent.Cancel -> {
                                findNavController().navigateUp()
                            }
                            is TaskAddScreenEvent.SetDeadline -> {
                                selectDeadline(event.task)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun selectDeadline(task: Task) {
        val timeZone = TimeZone.currentSystemDefault()
        val oldDeadline = task.deadline

        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(R.string.date_picker_description)
            .setSelection(
                oldDeadline?.toInstant(timeZone)?.toEpochMilliseconds() ?: Clock.System.now()
                    .toEpochMilliseconds()
            )
            .build()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText(R.string.time_picker_description)
            .setHour(oldDeadline?.hour ?: 12)
            .setMinute(oldDeadline?.minute ?: 0)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            timePicker.show(parentFragmentManager, "TimePicker")
        }
        datePicker.addOnCancelListener { task.deadline = null }
        datePicker.addOnNegativeButtonClickListener { task.deadline = null }

        timePicker.addOnPositiveButtonClickListener {
            var date: LocalDateTime
            datePicker.selection?.let { epoch ->
                date = Instant.fromEpochMilliseconds(epoch).toLocalDateTime(TimeZone.UTC)
                val datetime = LocalDateTime(
                    date.year,
                    date.month,
                    date.dayOfMonth,
                    timePicker.hour,
                    timePicker.minute
                )
                Log.d("TimePicker", datetime.toString())
                task.deadline = datetime
                task.timezone = timeZone
            }
        }
        timePicker.addOnCancelListener { task.deadline = null }
        timePicker.addOnNegativeButtonClickListener { task.deadline = null }

        datePicker.show(parentFragmentManager, "DatePicker")
    }
}