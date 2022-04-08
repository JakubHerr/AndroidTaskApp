package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasks.R
import com.example.tasks.data.AppDatabase
import com.example.tasks.databinding.FragmentEditTaskBinding
import com.example.tasks.viewmodels.TasksViewModel
import com.example.tasks.viewmodels.TasksViewModelFactory

class EditTaskFragment : Fragment() {
    private var _binding: FragmentEditTaskBinding? = null
    private val binding: FragmentEditTaskBinding get() = _binding!!

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //create viewmodel using its factory
        val application = requireNotNull(this.activity).application
        val dao = AppDatabase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(TasksViewModel::class.java)

        //load the task to edit
        viewModel.loadTask(args.taskId)

        //create binding and inflate layout
        _binding = FragmentEditTaskBinding.inflate(inflater,container,false)

        //bind the viewmodel to the layout
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.deleteButton.setOnClickListener {
            viewModel.deleteTask()
            findNavController().navigate(R.id.action_editTaskFragment_to_tasksFragment)
        }

        binding.editButton.setOnClickListener {
            viewModel.editTask()
            findNavController().navigate(R.id.action_editTaskFragment_to_tasksFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}