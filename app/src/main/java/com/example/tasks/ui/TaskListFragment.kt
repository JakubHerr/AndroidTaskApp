package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.adapters.TaskCategoryAdapter
import com.example.tasks.databinding.FragmentTaskListBinding
import com.example.tasks.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment() {
    private var _binding: FragmentTaskListBinding? = null
    private val binding: FragmentTaskListBinding
        get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bind the viewmodel to the layout
        binding.vm = viewModel

        setUpRecyclerView()

        //navigate to new task UI
        binding.addTaskFab.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_addTaskFragment)
        }
    }

    private fun setUpRecyclerView() {
        val adapter = TaskCategoryAdapter(viewLifecycleOwner, {
            val directions = TaskListFragmentDirections.actionTasksFragmentToEditTaskFragment(it)
            findNavController().navigate(directions)
        },{
            viewModel.completeTask(it)
        })
        binding.categoryRecycler.adapter = adapter
        adapter.submitList(viewModel.categories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}