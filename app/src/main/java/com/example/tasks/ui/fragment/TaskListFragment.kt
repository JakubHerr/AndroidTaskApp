package com.example.tasks.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.tasks.ui.screen.TaskListScreen
import com.example.tasks.ui.theme.TasksTheme
import com.example.tasks.ui.viewmodel.TaskListViewModel
import com.example.tasks.R
import com.example.tasks.ui.screen.TaskListEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment() {
    private val viewModel: TaskListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                TasksTheme {
                    TaskListScreen(viewModel.categories) { event ->
                        when(event) {
                            is TaskListEvent.AddTask -> {
                                findNavController().navigate(R.id.taskAddFragment)
                            }
                            is TaskListEvent.TaskCompleted -> {
                                viewModel.toggleTaskCompleted(event.id)
                            }
                            is TaskListEvent.ShowTaskDetail -> {
                                val action = TaskListFragmentDirections
                                    .actionTaskListFragmentToTaskEditFragment(event.id)
                                findNavController().navigate(action)
                            }
                            is TaskListEvent.ShowCompleted -> {
                                viewModel.toggleCompletedCategory()
                            }
                            is TaskListEvent.SortByDeadline -> {
                                Log.d("TaskListFragment","Sort by deadline pressed")
                                viewModel.selectDeadlineCategory()
                            }
                            is TaskListEvent.SortByPriority -> {
                                viewModel.selectPriorityCategory()
                            }
                        }
                    }
                }
            }
        }
    }
}