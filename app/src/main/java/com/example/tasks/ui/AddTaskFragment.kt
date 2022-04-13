package com.example.tasks.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.tasks.R
import com.example.tasks.data.Task
import com.example.tasks.extensions.toDate
import com.example.tasks.extensions.toTime

class AddTaskFragment : TaskFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task = Task()

        binding.apply {
            dateBtn.text = task.date.toDate()
            timeBtn.text = task.time.toTime()

            modifyTask.contentDescription = "Add task"
            modifyTask.setOnClickListener {
                task.taskName = this.taskName.text.toString()
                viewModel.addTask(task)
                findNavController().navigateUp()
            }
            modifyTask.setImageResource(R.drawable.ic_baseline_save_24)

            deleteButton.setOnClickListener {
                findNavController().navigateUp()
            }
            deleteButton.text = getString(R.string.cancel_button)
        }


    }
}