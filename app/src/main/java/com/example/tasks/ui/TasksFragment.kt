package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.databinding.FragmentTasksBinding
import com.example.tasks.adapters.TaskItemAdapter
import com.example.tasks.other.SortType
import com.example.tasks.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding: FragmentTasksBinding
        get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment
        _binding = FragmentTasksBinding.inflate(inflater,container,false)

        //bind the viewmodel to the layout
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setUpRecyclerView()

        //navigate to new task UI
        binding.addTaskFab.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_addTaskFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSorting()
    }

    private fun setUpRecyclerView() {
        //set adapter for recyclerview and create an observer for updating
        val adapter = TaskItemAdapter{
            val directions = TasksFragmentDirections.actionTasksFragmentToEditTaskFragment(it)
            findNavController().navigate(directions)
        }
        binding.tasksList.adapter = adapter

        //update recyclerView when data changes
        viewModel.tasks.observe(viewLifecycleOwner) { newData ->
            adapter.submitList(newData)
        }
    }

    private fun setUpSorting() {
        binding.sortSelection.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                binding.sortByPriority.id -> viewModel.sortBy(SortType.PRIORITY)
                binding.sortByNextDate.id -> viewModel.sortBy(SortType.DEADLINE)
                else -> viewModel.sortBy(SortType.DEFAULT)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}