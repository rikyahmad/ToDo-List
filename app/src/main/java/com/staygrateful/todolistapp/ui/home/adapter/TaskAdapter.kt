package com.staygrateful.todolistapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.databinding.ItemTaskLayoutBinding

/**
 * Adapter for managing task data in a RecyclerView.
 * Handles creating ViewHolders, binding task data to ViewHolder, and detecting changes in the list of tasks.
 * @param onClickListener A lambda function to handle item click events.
 */
class TaskAdapter(
    private val onClickListener: (Task) -> Unit,
    private val onCompletedChecked: (Task, Boolean) -> Unit
) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflate the item view layout and create a ViewHolder
        val binding =
            ItemTaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Bind task data to ViewHolder
        val task = getItem(position)
        holder.bind(task)
    }

    /**
     * ViewHolder class for managing task item views.
     * Handles binding task data and item click events.
     */
    inner class TaskViewHolder(private val binding: ItemTaskLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set click listener for task item
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = getItem(position)
                    onClickListener(task)
                }
            }

            binding.completeCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = getItem(position).apply {
                        isCompleted = isChecked
                    }
                    onCompletedChecked(task, isChecked)
                }
            }
        }

        /**
         * Binds task data to the item view.
         * @param task The task object to bind.
         */
        fun bind(task: Task) {
            // Bind task data to item view
            binding.taskNameTextView.text = task.title
            binding.dueDateTextView.text = task.formattedDueDate
            binding.completeCheckBox.isChecked = task.isCompleted
        }
    }

    /**
     * DiffUtil.ItemCallback implementation for calculating the difference between two lists of tasks.
     * Used to efficiently update the RecyclerView when the list of tasks changes.
     */
    private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            // Check if items have the same ID
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            // Check if item contents are the same
            return oldItem == newItem
        }
    }
}
