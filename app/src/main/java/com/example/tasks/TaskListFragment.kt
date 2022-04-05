package com.example.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tasks.databinding.FragmentTaskListBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.util.*

class TaskListFragment : Fragment() {
    var _binding: FragmentTaskListBinding? = null
    val binding: FragmentTaskListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater,container,false)

        //pick time and show it in a toast on success
        binding.fab.setOnClickListener {
            pickTime()
        }

        return binding.root
    }

    private fun pickTime() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(CLOCK_24H)
            .setHour(Calendar.getInstance().time.hours)
            .setMinute(Calendar.getInstance().time.minutes)
            .build()
        timePicker.show(childFragmentManager,"test")
        timePicker.addOnPositiveButtonClickListener {
            Toast.makeText(context,"Time picked: ${timePicker.hour}:${timePicker.minute}",Toast.LENGTH_LONG).show()
        }
    }
}