package com.staygrateful.todolistapp.ui.edit.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.widget.addTextChangedListener
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.data.model.Task
import com.staygrateful.todolistapp.databinding.ActivityEditTaskBinding
import com.staygrateful.todolistapp.external.extension.getFormattedDate
import com.staygrateful.todolistapp.external.extension.showDateTimePickerDialog
import com.staygrateful.todolistapp.external.extension.showSnackbar
import com.staygrateful.todolistapp.external.extension.showToast
import com.staygrateful.todolistapp.external.helper.SchedulerHelper
import com.staygrateful.todolistapp.ui.BaseActivity
import com.staygrateful.todolistapp.ui.edit.viewmodel.EditViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity for editing or creating tasks in the to-do list app.
 * Users can edit task details such as title, description, and due date.
 */
@AndroidEntryPoint
class EditTaskActivity : BaseActivity() {

    // ViewModel for managing data and business logic related to task editing
    private val viewModel by viewModels<EditViewModel>()

    // ViewBinding for accessing views in the layout file
    private val binding: ActivityEditTaskBinding by lazy {
        ActivityEditTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Retrieve task data from intent extras
        retrieveIntentExtra(intent)

        // Populate UI with initial task data
        setupInitialData()

        // Setup observer for detecting changes in UI elements
        setupObserver()

        // Setup event listeners for UI elements
        setupEventListener()
    }

    /**
     * Retrieves task data from intent extras, if provided.
     * If no task data is provided, initializes a new task.
     * @param intent The intent passed to the activity.
     */
    private fun retrieveIntentExtra(intent: Intent?) {
        if (intent == null) return
        // Get the task data from intent extras
        viewModel.task = IntentCompat.getParcelableExtra(intent, Task.KEY, Task::class.java)
            ?: Task.newInstance()
    }

    /**
     * Populates UI elements with initial task data.
     */
    private fun setupInitialData() {
        // Populate UI with task data
        binding.editTextTaskTitle.setText(viewModel.task.title)
        binding.editTextDescription.setText(viewModel.task.description)
        binding.textDate.text = viewModel.task.dueDate.getFormattedDate()
        binding.buttonCreateTask.text = ContextCompat.getString(
            this,
            if (viewModel.task.hasId) R.string.button_update_task else R.string.button_create_task
        )
    }

    /**
     * Sets up observer for detecting changes in UI elements.
     * Updates task data in ViewModel when user modifies task details.
     */
    private fun setupObserver() {
        binding.editTextTaskTitle.addTextChangedListener {
            viewModel.task.title = it.toString()
        }

        binding.editTextDescription.addTextChangedListener {
            viewModel.task.description = it.toString()
        }
    }

    /**
     * Sets up event listeners for UI elements.
     * Defines actions to be performed when UI elements are interacted with.
     */
    private fun setupEventListener() {
        binding.buttonCreateTask.setOnClickListener {
            // Save the task and handle errors
            viewModel.saveTask(viewModel.task) { error ->
                if (error != null) {
                    // Show error message in Snackbar if task saving fails
                    binding.root.showSnackbar(
                        getString(
                            R.string.error_message_task,
                            error.localizedMessage
                        )
                    )
                    return@saveTask
                }
                // Finish activity if task is saved successfully
                requestAlarmPermission { result ->
                    if (!result) {
                        return@requestAlarmPermission
                    }
                    SchedulerHelper.scheduleAlarm(
                        this,
                        viewModel.task,
                    )
                    finish()
                }
            }
        }

        binding.buttonCancel.setOnClickListener {
            // Finish activity when Cancel button is clicked
            finish()
        }

        binding.iconBack.setOnClickListener {
            // Finish activity when Cancel button is clicked
            finish()
        }

        binding.textDate.setOnClickListener {
            // Show date and time picker dialog when date TextView is clicked
            showDateTimePickerDialog(currentTimeInMillis = viewModel.task.dueDate) { timeInMillis, dateTime ->
                if(timeInMillis <= System.currentTimeMillis()) {
                    showToast(getString(R.string.error_form_past_date))
                    return@showDateTimePickerDialog
                }
                binding.textDate.text = dateTime
                viewModel.task.dueDate = timeInMillis
            }
        }
    }

    companion object {

        /**
         * Starts the EditTaskActivity with optional task data provided.
         * @param context The context from which the activity is started.
         * @param task The task data to be edited, or null if creating a new task.
         */
        fun start(context: Context, task: Task? = null) {
            // Start the EditTaskActivity with intent containing task data
            context.startActivity(Intent(context, EditTaskActivity::class.java).apply {
                putExtra(Task.KEY, task)
            })
        }
    }
}
