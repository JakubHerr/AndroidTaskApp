package com.example.tasks.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.tasks.R
import com.example.tasks.ui.screen.TaskAddScreen
import com.example.tasks.ui.theme.TasksTheme
import com.example.tasks.ui.viewmodel.TaskAddViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskAddFragment : Fragment() {
    private val viewModel: TaskAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            id = R.id.taskAddFragment

            setContent {
                TasksTheme {
                    TaskAddScreen(onTaskAdd = { task ->
                        viewModel.addTask(task)
                        findNavController().navigateUp()
                    }, onCancel = {
                        findNavController().navigateUp()
                    })
                }
            }
        }
    }
}