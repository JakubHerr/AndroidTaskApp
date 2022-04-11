package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasks.data.Task
import com.example.tasks.databinding.FragmentEditTaskBinding
import com.example.tasks.extensions.toDate
import com.example.tasks.viewmodels.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTaskFragment : Fragment() {
    private var _binding: FragmentEditTaskBinding? = null
    private val binding: FragmentEditTaskBinding get() = _binding!!

    lateinit var editableTask: Task

    private val args: EditTaskFragmentArgs by navArgs()
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //create binding and inflate layout
        _binding = FragmentEditTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.retrieveTask(args.taskId).observe(viewLifecycleOwner) {
            editableTask = it
            bind(it)
        }
    }

    private fun bind(task: Task) {
        binding.apply {
            taskName.setText(task.taskName, TextView.BufferType.EDITABLE)
            taskName.setSelection(task.taskName.length)
            dateBtn.text = task.date.toDate()
            dateBtn.setOnClickListener { setDate() }
            binding.editButton.setOnClickListener {
                editableTask.taskName = taskName.text.toString()
                viewModel.editTask(editableTask)
                findNavController().navigateUp()
            }
            binding.deleteButton.setOnClickListener {
                viewModel.deleteTask(editableTask)
                findNavController().navigateUp()
            }
        }
    }

    private fun setDate() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Choose date")
            .setSelection(if(editableTask.date == 0L) MaterialDatePicker.todayInUtcMilliseconds() else editableTask.date)
            .build()
        datePicker.show(childFragmentManager,"datePicker")
        datePicker.addOnPositiveButtonClickListener {
            editableTask.date = datePicker.selection ?: 0L
            binding.dateBtn.text = editableTask.date.toDate()
        }
        datePicker.addOnNegativeButtonClickListener {
            editableTask.date = 0L
            binding.dateBtn.text = 0L.toDate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}