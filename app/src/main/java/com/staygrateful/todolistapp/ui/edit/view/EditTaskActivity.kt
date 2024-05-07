package com.staygrateful.todolistapp.ui.edit.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.widget.addTextChangedListener
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.databinding.ActivityEditTaskBinding
import com.staygrateful.todolistapp.databinding.ActivityMainBinding
import com.staygrateful.todolistapp.ui.edit.viewmodel.EditViewModel
import com.staygrateful.todolistapp.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTaskActivity : AppCompatActivity() {

    // ViewModel for managing data and business logic related to the home screen
    private val viewModel by viewModels<EditViewModel>()

    // ViewBinding for accessing views in the layout file
    private val binding: ActivityEditTaskBinding by lazy {
        ActivityEditTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the task data from intent
        val task = IntentCompat.getParcelableExtra(intent, Task.KEY, Task::class.java)
            ?: Task.newInstance()

        // Populate UI with task data
        binding.editTextTaskTitle.setText(task.title)
        binding.editTextTaskDescription.setText(task.description)

        binding.editTextTaskTitle.addTextChangedListener {
            task.title = it.toString()
        }

        binding.editTextTaskDescription.addTextChangedListener {
            task.description = it.toString()
        }

        // Add your code here
        binding.buttonSaveTask.setOnClickListener {
            if (task.hasId) {
                viewModel.updateTask(task)
            } else {
                viewModel.insertTask(task)
            }
            finish()
        }
    }

    companion object {

        fun start(context: Context, task: Task? = null) {
            context.startActivity(Intent(context, EditTaskActivity::class.java).apply {
                putExtra(Task.KEY, task)
            })
        }
    }
}
