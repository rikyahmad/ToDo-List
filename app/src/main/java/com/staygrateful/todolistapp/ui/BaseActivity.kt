package com.staygrateful.todolistapp.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.external.extension.showToast

/**
 * Base activity class for TodoListApp activities.
 *
 * This abstract class serves as the base class for all activities in TodoListApp.
 * It provides common functionality for handling permissions and scheduling alarms.
 */
abstract class BaseActivity : AppCompatActivity() {

    // Activity result launcher for requesting alarm permission
    private val requestAlarmPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onAlarmPermissionResult.invoke(true)
            } else {
                showToast(getString(R.string.error_permission_exact_alarm))
                onAlarmPermissionResult.invoke(false)
            }
        }

    // Activity result launcher for requesting post notification permission
    private val requestPostPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                showToast(getString(R.string.error_permission_post_notification))
            }
        }

    private var onAlarmPermissionResult: (Boolean) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request post notification permission when activity is created
        requestPostPermission()
        // Request alarm permission when activity is created (optional)
        requestAlarmPermission()
    }

    // Method to request post notification permission
    private fun requestPostPermission() {
        // Request post notification permission only on Android Tiramisu (API level 33) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPostPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Method to request alarm permission
    fun requestAlarmPermission(onResult: (Boolean) -> Unit = {}) {
        // Request alarm permission only on Android S (API level 31) and above
        onAlarmPermissionResult = onResult
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestAlarmPermissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        } else {
            onResult.invoke(true)
        }
    }
}
