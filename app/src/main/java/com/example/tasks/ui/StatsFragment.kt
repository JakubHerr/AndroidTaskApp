package com.example.tasks.ui

import android.icu.util.Calendar
import android.os.Bundle
import android.os.CountDownTimer
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
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding: FragmentStatsBinding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()

    lateinit var nextTask: LiveData<Task>
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatsBinding.inflate(inflater,container,false)

        nextTask = viewModel.retrieveNextTask(Calendar.getInstance().timeInMillis)

        if(nextTask.value != null) setUpCountdown()
        else {
            binding.taskName.text = "You have no tasks"
            binding.nextDeadlineCountdown.text = ""
        }

        return binding.root
    }

    private fun setUpCountdown() {
        nextTask.observe(viewLifecycleOwner) {
            Log.d("StatsFragment","Next task has id ${it.taskId}")
            binding.taskName.text = it.taskName
            timer = object: CountDownTimer(it.deadline.timeInMillis - Calendar.getInstance().timeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.nextDeadlineCountdown.setText(SimpleDateFormat("HH:mm:ss", Locale.US).format(millisUntilFinished))
                }

                override fun onFinish() {
                    binding.nextDeadlineCountdown.setText("done!")
                }
            }.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        timer = null
        _binding = null
    }
}