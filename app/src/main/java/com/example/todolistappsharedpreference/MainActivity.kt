
package com.example.todolistappsharedpreference
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistappsharedpreference.R
import com.example.todolistappsharedpreference.Task
import com.example.todolistappsharedpreference.TaskAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var taskList: MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTaskEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        editTaskEditText = findViewById(R.id.editTaskEditText)
        recyclerView = findViewById(R.id.recyclerView)
        taskList = retrieveTasks()
        taskAdapter = TaskAdapter(taskList, object : TaskAdapter.TaskClickListener {
            override fun onEditClick(position: Int) {
                // Handle edit button click
                editTaskEditText.setText(taskList[position].title)
                taskList.removeAt(position)
                taskAdapter.notifyDataSetChanged()
            }

            override fun onDeleteClick(position: Int) {
                // Handle delete button click
                taskList.removeAt(position)
                taskAdapter.notifyDataSetChanged()
            }
        })

        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            val taskText = editTaskEditText.text.toString()
            if (taskText.isNotEmpty()) {
                val task = Task(taskText, false)
                taskList.add(task)
                saveTasks(taskList)
                taskAdapter.notifyDataSetChanged()
                editTaskEditText.text.clear()
            }
        }
    }

    private fun retrieveTasks(): MutableList<Task> {
        val tasks = sharedPreferences.getStringSet("tasks", HashSet()) ?: HashSet()
        return tasks.map { Task(it, false) }.toMutableList()
    }

    private fun saveTasks(taskList: List<Task>) {
        val editor = sharedPreferences.edit()
        val taskSet = HashSet<String>()
        taskList.forEach { taskSet.add(it.title) }
        editor.putStringSet("tasks", taskSet)
        editor.apply()
    }
}