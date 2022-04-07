package com.example.tasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.databinding.TaskItemBinding
import com.example.tasks.data.Task
import java.text.SimpleDateFormat

class TaskItemAdapter(val clickListener: (id: Long) -> Unit) : ListAdapter<Task, TaskItemAdapter.TaskItemViewHolder>(TaskDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder
            = TaskItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task,clickListener)
    }

    class TaskItemViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val sdf = SimpleDateFormat("dd/MM/yyyy")
        companion object{
            fun inflateFrom(parent: ViewGroup) : TaskItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(layoutInflater,parent,false)
                return TaskItemViewHolder(binding)
            }
        }

        fun bind(task: Task, clickListener: (id: Long) -> Unit) {
            binding.task = task
            task.date?.let {
                binding.date.text = sdf.format(it)
            }

            binding.root.setOnClickListener {
                clickListener(task.taskId)
            }
        }
    }
}

class TaskDiffItemCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = (oldItem.taskId == newItem.taskId)

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = (oldItem == newItem)
}