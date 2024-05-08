package com.staygrateful.todolistapp.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.staygrateful.todolistapp.external.helper.AlarmSchedulerHelper

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
                setAlarm()
            } else {
                Toast.makeText(this, "Permission SCHEDULE_EXACT_ALARM is required.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    // Activity result launcher for requesting post notification permission
    private val requestPostPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Permission POST_NOTIFICATIONS is required.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request post notification permission when activity is created
        requestPostPermission()
        // Request alarm permission when activity is created (optional)
        // requestAlarmPermission()
    }

    // Method to set an alarm
    private fun setAlarm() {
        AlarmSchedulerHelper.scheduleAlarm(this, 1, System.currentTimeMillis() + 15_000L, 3000L)
    }

    // Method to request post notification permission
    private fun requestPostPermission() {
        // Request post notification permission only on Android Tiramisu (API level 33) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPostPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Method to request alarm permission
    private fun requestAlarmPermission() {
        // Request alarm permission only on Android S (API level 31) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestAlarmPermissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        } else {
            // If alarm permission is not required, set the alarm directly
            setAlarm()
        }
    }
}
