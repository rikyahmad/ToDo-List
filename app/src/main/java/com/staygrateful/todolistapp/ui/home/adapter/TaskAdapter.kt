package com.staygrateful.todolistapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.databinding.TaskItemLayoutBinding

class TaskAdapter(private val onClickListener: (Task) -> Unit) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            TaskItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class TaskViewHolder(private val binding: TaskItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = getItem(position)
                    onClickListener(task)
                }
            }
        }

        fun bind(task: Task) {
            binding.taskNameTextView.text = task.title
            binding.dueDateTextView.text = task.formattedDueDate
            binding.completeCheckBox.isChecked = task.isCompleted
            binding.completeCheckBox.visibility =
                if (task.isCompleted) ViewGroup.VISIBLE else ViewGroup.GONE
        }
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}

