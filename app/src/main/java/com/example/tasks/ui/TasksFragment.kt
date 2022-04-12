package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.adapters.TaskCategoryAdapter
import com.example.tasks.databinding.FragmentTasksBinding
import com.example.tasks.adapters.TaskItemAdapter
import com.example.tasks.data.Task
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bind the viewmodel to the layout
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = TaskCategoryAdapter(viewLifecycleOwner) {
            val directions = TasksFragmentDirections.actionTasksFragmentToEditTaskFragment(it)
            findNavController().navigate(directions)
        }

        binding.categoryRecycler.adapter = adapter
        adapter.submitList(viewModel.categories)

        //navigate to new task UI
        binding.addTaskFab.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_addTaskFragment)
        }
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, source: LiveData<List<Task>>) {
        val adapter = TaskItemAdapter{

        }
        //set adapter for recyclerview
        recyclerView.adapter = adapter

        //set up source of data for adapter
        source.observe(viewLifecycleOwner) { newData ->
            adapter.submitList(newData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //    private fun setUpSorting() {
//        binding.sortSelection.setOnCheckedChangeListener { group, checkedId ->
//            when(checkedId) {
//                binding.sortByPriority.id -> viewModel.sortBy(SortType.PRIORITY)
//                binding.sortByNextDate.id -> viewModel.sortBy(SortType.FUTURE_DEADLINE)
//                else -> viewModel.sortBy(SortType.DEFAULT)
//            }
//        }
//    }
}