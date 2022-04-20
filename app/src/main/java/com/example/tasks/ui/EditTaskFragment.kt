package com.example.tasks.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasks.R
import com.example.tasks.extensions.toDeadline

class EditTaskFragment : TaskFragment() {
    private val args: EditTaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.retrieveTask(args.taskId).observe(viewLifecycleOwner) {
            task = it
            bind()
        }
        binding.deleteButton.text = getString(R.string.delete_button)
    }

    private fun bind() {
        binding.apply {
            taskName.setText(task.taskName, TextView.BufferType.EDITABLE)
            taskName.setSelection(task.taskName.length)

            deadlineBtn.text = task.deadline.timeInMillis.toDeadline()

            binding.modifyTask.setOnClickListener {
                task.taskName = taskName.text.toString()
                viewModel.editTask(task)
                findNavController().navigateUp()
            }
            binding.modifyTask.setImageResource(R.drawable.ic_baseline_edit_24)

            binding.deleteButton.setOnClickListener {
                viewModel.deleteTask(task)
                findNavController().navigateUp()
            }
        }
    }
}