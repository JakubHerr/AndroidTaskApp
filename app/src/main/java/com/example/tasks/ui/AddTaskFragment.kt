package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.tasks.R
import androidx.navigation.fragment.findNavController
import com.example.tasks.databinding.FragmentAddTaskBinding
import com.example.tasks.viewmodels.AddTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding: FragmentAddTaskBinding get() = _binding!!

    private val viewModel: AddTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(inflater,container,false)

        //bind the viewmodel to the layout
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //set up date picking "databinding"
        binding.dateBtn.setOnClickListener {
            viewModel.setDate(childFragmentManager)
        }

        viewModel.date.observe(viewLifecycleOwner) {
            binding.dateBtn.text = getString(R.string.no_date_selected)
            it?.let {
                binding.dateBtn.text = SimpleDateFormat("dd/MM/yyyy").format(it)
            }
        }

        //navigate back when necessary
        viewModel.navigateBack.observe(viewLifecycleOwner) {
            if(it) {
                viewModel.onNavigateBack()
                findNavController().navigate(R.id.action_addTaskFragment_to_tasksFragment)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}