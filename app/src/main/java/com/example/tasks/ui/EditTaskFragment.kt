package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasks.R
import com.example.tasks.data.TaskDao
import com.example.tasks.databinding.FragmentEditTaskBinding
import com.example.tasks.viewmodels.EditTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditTaskFragment : Fragment() {
    private var _binding: FragmentEditTaskBinding? = null
    private val binding: FragmentEditTaskBinding get() = _binding!!

    @Inject
    lateinit var dao: TaskDao

    private val args: EditTaskFragmentArgs by navArgs()
    private lateinit var viewModel: EditTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //create binding and inflate layout
        _binding = FragmentEditTaskBinding.inflate(inflater,container,false)

        //bind the viewmodel to the layout
        viewModel = EditTaskViewModel(dao,args.taskId)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //launch date picker
        binding.dateBtn.setOnClickListener {
            viewModel.setDate(childFragmentManager)
        }

        //navigate back when necessary
        viewModel.navigateBack.observe(viewLifecycleOwner) {
            if(it) {
                viewModel.onNavigateBack()
                findNavController().navigate(R.id.action_editTaskFragment_to_tasksFragment)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}