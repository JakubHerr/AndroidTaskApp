package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.example.tasks.databinding.FragmentTaskListBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.util.*
import com.example.tasks.adapters.TaskItemAdapter
import com.example.tasks.data.AppDatabase
import com.example.tasks.viewmodels.TasksViewModel
import com.example.tasks.viewmodels.TasksViewModelFactory

class TasksFragment : Fragment() {
    var _binding: FragmentTaskListBinding? = null
    val binding: FragmentTaskListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater,container,false)

        //create viewmodel using its factory
        val application = requireNotNull(this.activity).application
        val dao = AppDatabase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(TasksViewModel::class.java)

        //bind the viewmodel to the layout
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //set adapter for recyclerview and create an observer for updating
        val adapter = TaskItemAdapter{}

        binding.tasksList.adapter = adapter
        viewModel.tasks.observe(viewLifecycleOwner, Observer { newData ->
            adapter.submitList(newData)
        })

        //pick time and show it in a toast on success
        binding.fab.setOnClickListener {
            pickTime()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pickTime() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(CLOCK_24H)
            .setHour(Calendar.getInstance().time.hours)
            .setMinute(Calendar.getInstance().time.minutes)
            .build()
        timePicker.show(childFragmentManager,"test")
        timePicker.addOnPositiveButtonClickListener {
            Toast.makeText(context,"Time picked: ${timePicker.hour}:${timePicker.minute}",Toast.LENGTH_LONG).show()
        }
    }
}