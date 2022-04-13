package com.example.tasks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.data.Category
import com.example.tasks.databinding.CategoryItemBinding

class TaskCategoryAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val clickListener: (id: Long) -> Unit,
    private val checkboxClickListener: (id: Long) -> Unit,)
    : ListAdapter<Category,TaskCategoryAdapter.TaskCategoryViewHolder>(CategoryDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCategoryViewHolder
            = TaskCategoryViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: TaskCategoryViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task,lifecycleOwner,clickListener,checkboxClickListener)
    }

    class TaskCategoryViewHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun inflateFrom(parent: ViewGroup) : TaskCategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CategoryItemBinding.inflate(layoutInflater,parent,false)
                return TaskCategoryViewHolder(binding)
            }
        }

        fun bind(category: Category,lifecycleOwner: LifecycleOwner, clickListener: (id: Long) -> Unit, checkboxClickListener: (id: Long) -> Unit) {
            binding.category = category

            val taskAdapter = TaskItemAdapter(clickListener, checkboxClickListener)

            binding.collapseButton.setOnClickListener {
                binding.taskList.visibility = when(binding.taskList.visibility) {
                    View.VISIBLE -> View.GONE
                    else -> View.VISIBLE
                }
            }

            binding.taskList.adapter = taskAdapter
            category.tasks.observe(lifecycleOwner) { newData ->
                //TODO fix, card is not visible, but leaves a gap in recyclerview
                binding.card.visibility  = if(newData.isEmpty()) View.GONE else View.VISIBLE
                taskAdapter.submitList(newData)
            }

        }
    }
}

class CategoryDiffItemCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldCategory: Category, newCategory: Category): Boolean = (oldCategory.name == newCategory.name)

    override fun areContentsTheSame(oldCategory: Category, newCategory: Category): Boolean = (oldCategory == newCategory)
}