package com.staygrateful.todolistapp.ui.edit.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.IntentCompat
import androidx.core.widget.addTextChangedListener
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.databinding.ActivityEditTaskBinding
import com.staygrateful.todolistapp.external.extension.showDateTimePickerDialog
import com.staygrateful.todolistapp.external.extension.showSnackbar
import com.staygrateful.todolistapp.external.extension.showToast
import com.staygrateful.todolistapp.ui.BaseActivity
import com.staygrateful.todolistapp.ui.edit.viewmodel.EditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTaskActivity : BaseActivity() {

    // ViewModel for managing data and business logic related to the home screen
    private val viewModel by viewModels<EditViewModel>()

    // ViewBinding for accessing views in the layout file
    private val binding: ActivityEditTaskBinding by lazy {
        ActivityEditTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        retrieveIntentExtra(intent)
        setupInitialData()
        setupObserver()
        setupEventListener()
    }

    private fun retrieveIntentExtra(intent: Intent?) {
        if(intent == null) return
        // Get the task data from intent
        viewModel.task = IntentCompat.getParcelableExtra(intent, Task.KEY, Task::class.java)
            ?: Task.newInstance()
    }

    private fun setupInitialData() {
        // Populate UI with task data
        binding.editTextTaskTitle.setText(viewModel.task.title)
        binding.editTextDescription.setText(viewModel.task.description)
    }

    private fun setupObserver() {
        binding.editTextTaskTitle.addTextChangedListener {
            viewModel.task.title = it.toString()
        }

        binding.editTextDescription.addTextChangedListener {
            viewModel.task.description = it.toString()
        }
    }

    private fun setupEventListener() {
        // Add your code here
        binding.buttonCreateTask.setOnClickListener {
            viewModel.saveTask(viewModel.task) { error ->
                if(error != null) {
                    binding.root.showSnackbar(
                        getString(
                            R.string.error_message_task,
                            error.localizedMessage
                        ))
                    return@saveTask
                }
                finish()
            }
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }

        binding.textDate.setOnClickListener {
            showDateTimePickerDialog { timeInMillis, dateTime ->
                binding.textDate.text = dateTime
                viewModel.task.dueDate = timeInMillis
            }
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
