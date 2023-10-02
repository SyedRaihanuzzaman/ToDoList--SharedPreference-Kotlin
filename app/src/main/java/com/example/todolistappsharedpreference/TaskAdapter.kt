package com.example.todolistappsharedpreference

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistappsharedpreference.databinding.ItemTaskBinding

class TaskAdapter(private val taskList: MutableList<Task>, private val clickListener: TaskClickListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface TaskClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
        holder.itemView.setOnClickListener {
            clickListener.onEditClick(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            clickListener.onDeleteClick(position)
        }
        // Set the checkbox state based on the task's completion status
        holder.binding.checkBox.isChecked = task.isCompleted

        // Handle checkbox click event
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            // You can implement saving the completion status to SharedPreferences here if needed
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title
        }
    }
}
