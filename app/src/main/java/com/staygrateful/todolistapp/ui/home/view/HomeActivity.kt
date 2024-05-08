package com.staygrateful.todolistapp.ui.home.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.databinding.ActivityMainBinding
import com.staygrateful.todolistapp.external.callback.SwipeToDeleteCallback
import com.staygrateful.todolistapp.external.extension.showSnackbar
import com.staygrateful.todolistapp.external.helper.SchedulerHelper
import com.staygrateful.todolistapp.ui.BaseActivity
import com.staygrateful.todolistapp.ui.edit.view.EditTaskActivity
import com.staygrateful.todolistapp.ui.home.adapter.TaskAdapter
import com.staygrateful.todolistapp.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity for displaying the home screen of the to-do list app.
 * Users can view, add, and delete tasks from this screen.
 */
@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    // ViewModel for managing data and business logic related to the home screen
    private val homeViewModel by viewModels<HomeViewModel>()

    // ViewBinding for accessing views in the layout file
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Adapter for managing task data in the RecyclerView
    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(
            onClickListener = { task ->
                EditTaskActivity.start(this, task)
            },
            onCompletedChecked = { task, isChecked ->
                homeViewModel.updateTask(task)
                if (isChecked) {
                    SchedulerHelper.cancelAlarm(this, task.id)
                } else {
                    SchedulerHelper.scheduleAlarm(this, task)
                }
            }
        )
    }

    // BroadcastReceiver for handling task update events
    private val taskUpdatedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Handle task updated event
            val taskId = intent?.getLongExtra(SchedulerHelper.TASK_ID, -1)
            if (taskId != null && taskId != -1L) {
                // Task with taskId is updated, update UI accordingly
                // For example, refresh the task list
                homeViewModel.getAllTasks(binding.searchEditText.text.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Receive and handle intent extras, if any
        receiveIntentExtra(intent)

        // Enable edge-to-edge display for immersive experience
        enableEdgeToEdge()

        // Set the layout using ViewBinding
        setContentView(binding.root)

        // Apply window insets to adjust padding for system bars
        applyWindowInsets()

        // Setup RecyclerView for displaying tasks
        setupTaskRecyclerView()

        // Setup click listener for the "Add Task" button
        setupButtonListener()

        // Setup observer for LiveData to update UI when task data changes
        setupObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(taskUpdatedReceiver)
    }

    /**
     * Receives and handles intent extras, if any, passed to the activity.
     * This function is called during the activity's initialization.
     * It checks if there are any intent extras related to tasks,
     * such as task IDs for scheduling alarms or notifications.
     * @param intent The intent passed to the activity.
     */
    private fun receiveIntentExtra(intent: Intent) {
        // Check if the intent contains a task ID for scheduling alarms
        if (intent.getLongExtra(SchedulerHelper.TASK_ID, -1L) > 0L) {
            // such as scheduling alarms or displaying specific tasks.
            // Example: Extract task ID and perform relevant actions.
            val taskId = intent.getLongExtra(SchedulerHelper.TASK_ID, -1L)
            // Perform actions based on the task ID, such as scheduling alarms or displaying the task.
        }
    }

    /**
     * Observes changes in task data and updates the UI accordingly.
     */
    private fun setupObserver() {
        binding.searchEditText.addTextChangedListener {
            homeViewModel.getAllTasks(it.toString())
        }
        homeViewModel.allTasks.observe(this) { tasks ->
            with(tasks) {
                taskAdapter.submitList(this)
                binding.contentEmpty.textMessage.setText(
                    ContextCompat.getString(
                        this@HomeActivity, if (binding.searchEditText.text.toString()
                                .isEmpty()
                        ) R.string.error_message_empty_task else R.string.error_message_found_task
                    )
                )
                binding.contentEmpty.root.visibility =
                    if (tasks.isEmpty()) View.VISIBLE else View.GONE
            }
        }
        // Register broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(
            taskUpdatedReceiver,
            IntentFilter(SchedulerHelper.TASK_UPDATE)
        )
    }

    /**
     * Sets up click listener for the "Add Task" button.
     */
    private fun setupButtonListener() {
        binding.addTaskButton.setOnClickListener {
            EditTaskActivity.start(this)
        }
    }

    /**
     * Applies window insets to adjust padding for system bars.
     */
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Sets up RecyclerView to display tasks and handles swipe-to-delete functionality.
     */
    private fun setupTaskRecyclerView() {
        binding.taskRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = taskAdapter
            // Attach swipe-to-delete functionality using ItemTouchHelper
            ItemTouchHelper(SwipeToDeleteCallback(this@HomeActivity) { position ->
                val task = taskAdapter.currentList[position]
                SchedulerHelper.cancelAlarm(this@HomeActivity, task.id)
                // Delete the task from the database
                homeViewModel.deleteTask(task)
                // Show a Snackbar with "Undo" option for undoing deletion
                binding.root.showSnackbar(
                    context.getString(R.string.task_deleted),
                    context.getString(R.string.undo)
                ) {
                    // Implement logic to undo deletion
                    homeViewModel.insertTask(task)
                    SchedulerHelper.scheduleAlarm(this@HomeActivity, task)
                }
            }).attachToRecyclerView(this)
        }
    }
}
