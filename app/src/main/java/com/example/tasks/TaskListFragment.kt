package com.example.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tasks.databinding.FragmentTaskListBinding

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
        return binding.root
    }
}