package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.databinding.FragmentTasksBinding
import com.example.tasks.adapters.TaskItemAdapter
import com.example.tasks.data.AppDatabase
import com.example.tasks.viewmodels.TasksViewModel
import com.example.tasks.viewmodels.TasksViewModelFactory

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding: FragmentTasksBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment
        _binding = FragmentTasksBinding.inflate(inflater,container,false)

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

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_addTaskFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}