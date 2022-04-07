package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tasks.R
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tasks.data.AppDatabase
import com.example.tasks.databinding.FragmentAddTaskBinding
import com.example.tasks.viewmodels.TasksViewModel
import com.example.tasks.viewmodels.TasksViewModelFactory

class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding: FragmentAddTaskBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(inflater,container,false)

        //create viewmodel using its factory
        val application = requireNotNull(this.activity).application
        val dao = AppDatabase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(TasksViewModel::class.java)

        //bind the viewmodel to the layout
        binding.vm = viewModel

        //add new item and navigate back
        binding.saveButton.setOnClickListener {
            viewModel.addTask()
            findNavController().navigate(R.id.action_addTaskFragment_to_tasksFragment)
        }

        binding.dateBtn.setOnClickListener {
            viewModel.getDate(childFragmentManager)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}