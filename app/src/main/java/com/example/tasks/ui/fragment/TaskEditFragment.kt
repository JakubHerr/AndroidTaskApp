package com.example.tasks.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasks.ui.screen.TaskEditScreen
import com.example.tasks.ui.screen.TaskEditScreenEvent
import com.example.tasks.ui.theme.TasksTheme
import com.example.tasks.ui.viewmodel.TaskEditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskEditFragment : Fragment() {
    private val args: TaskEditFragmentArgs by navArgs()
    private val viewModel: TaskEditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val task = viewModel.retrieveTask(args.id)
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                TasksTheme {
                    TaskEditScreen(task) { event ->
                        when(event) {
                            is TaskEditScreenEvent.DeleteTask -> {
                                viewModel.deleteTask(event.task)
                                findNavController().navigateUp()
                            }
                            is TaskEditScreenEvent.EditTask -> {
                                viewModel.editTask(event.task)
                                findNavController().navigateUp()
                            }
                        }

                    }
                }
            }
        }
    }
}