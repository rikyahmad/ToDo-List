package com.staygrateful.todolistapp.ui.home.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.databinding.ActivityMainBinding
import com.staygrateful.todolistapp.external.callback.SwipeToDeleteCallback
import com.staygrateful.todolistapp.external.extension.showSnackbar
import com.staygrateful.todolistapp.external.helper.AlarmSchedulerHelper
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
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun receiveIntentExtra(intent: Intent) {
        if (intent.getLongExtra(AlarmSchedulerHelper.TASK_ID, -1L) > 0L) {
            // TODO:
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
            tasks?.let {
                taskAdapter.submitList(it)
            }
        }
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
                // Delete the task from the database
                homeViewModel.deleteTask(task)
                // Show a Snackbar with "Undo" option for undoing deletion
                binding.root.showSnackbar("Task deleted", "Undo") {
                    // Implement logic to undo deletion
                    homeViewModel.insertTask(task)
                }
            }).attachToRecyclerView(this)
        }
    }
}
