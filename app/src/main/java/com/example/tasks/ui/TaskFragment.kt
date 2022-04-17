package com.example.tasks.ui

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tasks.R
import com.example.tasks.data.Task
import com.example.tasks.databinding.ModifyTaskBinding
import com.example.tasks.extensions.toDate
import com.example.tasks.extensions.toTime
import com.example.tasks.other.Constants
import com.example.tasks.other.Constants.HIGH_PRIORITY
import com.example.tasks.other.Constants.LOW_PRIORITY
import com.example.tasks.other.Constants.MEDIUM_PRIORITY
import com.example.tasks.other.Constants.NO_PRIORITY
import com.example.tasks.viewmodels.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract  class TaskFragment: Fragment() {
    private var _binding: ModifyTaskBinding? = null
    protected val binding: ModifyTaskBinding get() = _binding!!

    protected val viewModel: TaskViewModel by viewModels()

    lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //create binding and inflate layout
        _binding = ModifyTaskBinding.inflate(inflater,container,false)

        binding.dateBtn.setOnClickListener { setDate() }
        binding.timeBtn.setOnClickListener { setTime() }
        binding.priority.setOnClickListener { setPriority(it,R.menu.menu_priority) }

        return binding.root
    }

    private fun setDate() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Choose date")
            .setSelection(if(task.date == 0L) MaterialDatePicker.todayInUtcMilliseconds() else task.date)
            .build()
        datePicker.show(childFragmentManager,"datePicker")
        datePicker.addOnPositiveButtonClickListener {
            task.date = datePicker.selection ?: 0L
            binding.dateBtn.text = task.date.toDate()
        }
        datePicker.addOnNegativeButtonClickListener {
            task.date = 0L
            binding.dateBtn.text = task.date.toDate()
        }
    }

    private fun setTime() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Set task time")
            .setTimeFormat(if (DateFormat.is24HourFormat(context)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()
        timePicker.show(childFragmentManager,"Add task timePicker")

        timePicker.addOnPositiveButtonClickListener {
            task.time = (60*timePicker.hour.toLong()+timePicker.minute) * Constants.MILLIS_IN_MINUTE
            binding.timeBtn.text = task.time.toTime()
        }
        timePicker.addOnNegativeButtonClickListener {
            task.time = 0L
            binding.timeBtn.text = task.time.toTime()
        }
    }

    private fun setPriority(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.high_priority -> {
                    task.priority = HIGH_PRIORITY
                    true
                }
                R.id.medium_priority -> {
                    task.priority = MEDIUM_PRIORITY
                    true
                }
                R.id.low_priority -> {
                    task.priority = LOW_PRIORITY
                    true
                }
                else -> {
                    task.priority = NO_PRIORITY
                    false
                }
            }
        }
        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}