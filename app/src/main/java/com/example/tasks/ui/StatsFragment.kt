package com.example.tasks.ui

import android.icu.util.Calendar
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.example.tasks.data.Task
import com.example.tasks.databinding.FragmentStatsBinding
import com.example.tasks.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding: FragmentStatsBinding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()

    lateinit var nextTask: LiveData<Task>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatsBinding.inflate(inflater,container,false)

        nextTask = viewModel.retrieveNextTask(Calendar.getInstance().timeInMillis)

        nextTask.observe(viewLifecycleOwner) {
            if(nextTask.value != null) setUpCountdown(it)
            else {
                binding.prompt.text = "You have no future deadlines"
                binding.taskName.visibility = View.GONE
                binding.countdown.visibility = View.GONE
            }
        }

        return binding.root
    }

    private fun setUpCountdown(task: Task) {
        Log.d("StatsFragment","Next task has id ${task.taskId}")
        binding.taskName.text = task.taskName
        binding.countdown.visibility = View.VISIBLE
        //Chronometer takes current milliseconds from boot as t=0, then the duration is added
        binding.countdown.base = SystemClock.elapsedRealtime() + (task.deadline.timeInMillis - Calendar.getInstance().timeInMillis)
        binding.countdown.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}