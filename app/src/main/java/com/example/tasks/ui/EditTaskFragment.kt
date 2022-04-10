package com.example.tasks.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasks.R
import com.example.tasks.databinding.FragmentEditTaskBinding
import com.example.tasks.viewmodels.TasksViewModel
import com.example.tasks.viewmodels.TasksViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class EditTaskFragment : Fragment() {
    private var _binding: FragmentEditTaskBinding? = null
    private val binding: FragmentEditTaskBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: TasksViewModelFactory

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //create viewmodel using its factory
        val viewModel = ViewModelProvider(this,viewModelFactory).get(TasksViewModel::class.java)

        //load the task to edit
        viewModel.loadTask(args.taskId)

        //create binding and inflate layout
        _binding = FragmentEditTaskBinding.inflate(inflater,container,false)

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