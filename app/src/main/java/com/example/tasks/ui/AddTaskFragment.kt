package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tasks.data.Task
import com.example.tasks.databinding.FragmentAddTaskBinding
import com.example.tasks.extensions.toDate
import com.example.tasks.viewmodels.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding: FragmentAddTaskBinding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()
    private val newTask = Task()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(inflater,container,false)

        //bind the viewmodel to the layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        binding.dateBtn.text = newTask.date.toDate()
        binding.dateBtn.setOnClickListener {
            setDate()
        }
        binding.saveButton.setOnClickListener {
            newTask.taskName = binding.taskName.text.toString()
            viewModel.addTask(newTask)
            findNavController().navigateUp()
        }
    }

    private fun setDate() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Choose date")
            .setSelection(if(newTask.date == 0L) MaterialDatePicker.todayInUtcMilliseconds() else newTask.date)
            .build()
        datePicker.show(childFragmentManager,"datePicker")
        datePicker.addOnPositiveButtonClickListener {
            newTask.date = datePicker.selection ?: 0L
            binding.dateBtn.text = newTask.date.toDate()
        }
        datePicker.addOnNegativeButtonClickListener {
            newTask.date = 0L
            binding.dateBtn.text = newTask.date.toDate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}